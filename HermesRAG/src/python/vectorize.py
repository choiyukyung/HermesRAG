import requests
import numpy as np
from sentence_transformers import SentenceTransformer
from typing import List, Tuple
from config import DB_CONFIG, QDRANT_SERVER_HOST, QDRANT_SERVER_PORT  # config.py에서 설정 가져오기
from qdrant_client import QdrantClient
from qdrant_client.models import PointStruct
import uuid


class ArticleVectorizer:
    def __init__(self, model_name: str = 'paraphrase-multilingual-mpnet-base-v2'):
        # model_name: SentenceTransformer
        self.model = SentenceTransformer(model_name)
        self.db_config = DB_CONFIG

    def get_article_from_api(self, api_url: str) -> List[Tuple[str, str, str]]:
        # Spring Boot API에서 뉴스 데이터 가져오기
        response = requests.get(api_url)
        article_data = response.json()
        return [(item['id'], item['webTitle'], item['trailText'], item['webUrl'], item['webPublicationDate']) for item in article_data]

    def vectorize_text(self, text: str) -> np.ndarray:
        # 텍스트 벡터화
        return self.model.encode(text)

    def save_vectors_to_qdrant(self, article_id: str, web_title_vector: np.ndarray, trail_text_vector: np.ndarray, web_title: str, trail_text: str, web_url: str, web_publication_date: str):
        # Docker에서 실행 중인 Qdrant 서버에 연결
        client = QdrantClient(host=QDRANT_SERVER_HOST, port=QDRANT_SERVER_PORT)

        # Qdrant 컬렉션 생성
        try:
            client.get_collection(collection_name="articles")
        except Exception as e:
            client.create_collection(
                collection_name="articles",
                vectors_config={"size": 768, "distance": "Cosine"},  # 임베딩 설정
            )

        # article_id를 UUID로 변환
        article_id_uuid = str(uuid.uuid5(uuid.NAMESPACE_DNS, article_id))

        # trail_id는 UUID에 접미사를 추가하여 고유한 ID 생성
        trail_id_uuid = str(uuid.uuid5(uuid.NAMESPACE_DNS, f"{article_id}_trail"))

        # 벡터 저장
        client.upsert(
            collection_name="articles",
            points=[
                PointStruct(
                    id=article_id_uuid,
                    vector=web_title_vector.tolist(),  # numpy → 리스트 변환
                    payload={
                        "type": "web_title",
                        "article_id": article_id,
                        "web_title": web_title,
                        "trail_text": trail_text,
                        "web_url": web_url,
                        "web_publication_date": web_publication_date
                    } # 벡터 임베딩값과 같이 추가 데이터 저장 가능(속도 영향 적음)
                ),
                PointStruct(
                    id=trail_id_uuid,  # trail_text는 id를 다르게 설정
                    vector=trail_text_vector.tolist(),
                    payload={
                        "type": "web_title",
                        "article_id": article_id,
                        "web_title": web_title,
                        "trail_text": trail_text,
                        "web_url": web_url,
                        "web_publication_date": web_publication_date
                    }
                )
            ]
        )

    def process_article(self, api_url: str):
        # 전체 프로세스 실행
        article_items = self.get_article_from_api(api_url)
        processed_count = 0  # 처리된 기사 수

        for article_id, web_title, trail_text, web_url, web_publication_date in article_items:
            # 제목과 내용 벡터화
            web_title_vector = self.vectorize_text(web_title)
            trail_text_vector = self.vectorize_text(trail_text)

            # DB에 저장
            self.save_vectors_to_qdrant(article_id, web_title_vector, trail_text_vector, web_title, trail_text, web_url, web_publication_date)
            processed_count += 1

        return {
            "status": "success",
            "processed_count": processed_count
        }

# 스크립트를 직접 실행할 때만 작동하는 코드 -> FastAPI 엔드포인트에서 직접 실행
# if __name__ == "__main__":
#     vectorizer = ArticleVectorizer()
#     result = vectorizer.process_article(API_URL_VECTORIZE)
#
#     print(json.dumps(result))