import requests
import numpy as np
from config import API_URL_SEARCH
import faiss
import base64

class FaissIndexer:
    def __init__(self, dimension=768):
        self.dimension = dimension
        self.index = faiss.IndexFlatL2(dimension)  # L2 거리 기반 인덱스
        self.articles = []  # 기사 데이터 저장용 리스트

        # 초기화 과정에서 인덱스 생성
        self.load_vectors_from_db()

    def decode_embedding(self, embedding_str):
        # Base64로 인코딩된 임베딩을 복원하여 numpy 배열로 변환
        try:
            decoded_bytes = base64.b64decode(embedding_str)
            float_array = np.frombuffer(decoded_bytes, dtype=np.float32)
            return float_array
        except Exception as e:
            print(f"error while decoding: {e}")
            return np.zeros(self.dimension, dtype=np.float32)  # 오류 발생 시 0 벡터 반환


    def load_vectors_from_db(self):
        # API에서 벡터 데이터를 가져와 FAISS 인덱스에 추가
        try:
            response = requests.get(API_URL_SEARCH, timeout=5)
            response.raise_for_status()  # 요청 실패 시 예외

            article_data = response.json()
            if not article_data:
                print("Warning: No article data received from API")
                return

            self.articles = article_data
            article_vectors = np.array(
                [self.decode_embedding(article["trailTextEmbedding"]) for article in article_data]
            )

            if article_vectors.shape[0] > 0:  # 벡터가 존재하는 경우에만 추가
                self.index.add(article_vectors)
            else:
                print("Warning: No valid vectors to add to FAISS index")

        except requests.RequestException as e:
            print(f"Error fetching vectors from API: {e}")

    def get_index(self):
        return self.index

    def get_articles(self):
        # 기사 데이터
        return self.articles