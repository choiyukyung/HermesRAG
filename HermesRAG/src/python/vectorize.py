import requests
import numpy as np
from sentence_transformers import SentenceTransformer
import mysql.connector
from typing import List, Tuple
from config import DB_CONFIG, API_URL  # config.py에서 설정 가져오기
import json

class ArticleVectorizer:
    def __init__(self, model_name: str = 'paraphrase-multilingual-mpnet-base-v2'):
        # model_name: SentenceTransformer
        self.model = SentenceTransformer(model_name)
        self.db_config = DB_CONFIG

    def get_article_from_api(self, api_url: str) -> List[Tuple[str, str, str]]:
        # Spring Boot API에서 뉴스 데이터 가져오기
        response = requests.get(api_url)
        article_data = response.json()
        # article 테이블의 id, web_title, trail_text 사용
        return [(item['id'], item['webTitle'], item['trailText']) for item in article_data]

    def vectorize_text(self, text: str) -> np.ndarray:
        # 텍스트 벡터화
        return self.model.encode(text)

    def save_vectors_to_db(self, article_id: str, web_title_vector: np.ndarray, trail_text_vector: np.ndarray):
        # 벡터화된 데이터를 DB에 저장
        vector_bytes_web_title = web_title_vector.tobytes()
        vector_bytes_trail_text = trail_text_vector.tobytes()

        conn = mysql.connector.connect(**self.db_config)
        cursor = conn.cursor()

        try:
            # 테이블명과 컬럼명을 실제 DB 스키마에 맞게 수정
            query = """
                UPDATE article
                SET web_title_embedding = %s, trail_text_embedding = %s
                WHERE id = %s
            """
            cursor.execute(query, (vector_bytes_web_title, vector_bytes_trail_text, article_id))
            conn.commit()
        finally:
            cursor.close()
            conn.close()

    def process_article(self, api_url: str):
        # 전체 프로세스 실행
        article_items = self.get_article_from_api(api_url)
        processed_count = 0  # 처리된 기사 수

        for article_id, web_title, trail_text in article_items:
            # 제목과 내용 벡터화
            web_title_vector = self.vectorize_text(web_title)
            trail_text_vector = self.vectorize_text(trail_text)

            # DB에 저장
            self.save_vectors_to_db(article_id, web_title_vector, trail_text_vector)
            processed_count += 1

        return {
            "status": "success",
            "processed_count": processed_count
        }


if __name__ == "__main__":
    vectorizer = ArticleVectorizer()
    result = vectorizer.process_article(API_URL)

    print(json.dumps(result))