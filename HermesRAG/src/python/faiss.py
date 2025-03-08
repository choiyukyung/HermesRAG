import requests
import numpy as np
from config import API_URL_FAISS
import base64

class FaissIndexing:
    def load_vectors_from_db(self) -> list:
        # 벡터 데이터를 API에서 가져오기
        response = requests.get(API_URL_FAISS)
        if response.status_code != 200:
            print(f"Error loading vectors: {response.text}")
            return []

        article_data = response.json()
        vectors = []

        for item in article_data:
            try:
                # base64로 인코딩된 벡터를 디코딩하여 numpy 배열로 변환
                web_title_vector = np.frombuffer(base64.b64decode(item['webTitleEmbedding']), dtype=np.float32)
                trail_text_vector = np.frombuffer(base64.b64decode(item['trailTextEmbedding']), dtype=np.float32)
                vectors.append((web_title_vector, trail_text_vector))
            except Exception as e:
                print(f"Error processing vectors for article {item['id']}: {e}")
                continue

        return vectors
