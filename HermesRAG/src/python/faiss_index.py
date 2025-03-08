import requests
import numpy as np
from config import API_URL_FAISS
import base64
import faiss

class FaissIndexer:
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

    def create_faiss_index(self, vectors: list):
        # 벡터를 FAISS 인덱스로 변환
        if not vectors:
            print("No vectors to index.")
            return None

        # 벡터 차원(d) 추출
        d = len(vectors[0][0])  # 첫 번째 벡터의 차원

        # FAISS 인덱스 생성 (Inner Product 방식으로)
        index = faiss.IndexFlatIP(d)

        # 벡터들만 2D 배열로 변환하여 FAISS에 추가
        all_vectors = np.array([vec[0] for vec in vectors] + [vec[1] for vec in vectors])  # 제목과 내용 벡터 모두
        index.add(all_vectors.astype(np.float32))

        print(f"FAISS index created with {index.ntotal} vectors.")
        return index

if __name__ == "__main__":
    loader = FaissIndexer()
    vectors = loader.load_vectors_from_db()

    # FAISS 인덱스 생성
    faiss_index = loader.create_faiss_index(vectors)

    if faiss_index:
        print(f"FAISS index successfully created and contains {faiss_index.ntotal} vectors.")
    else:
        print("Failed to create FAISS index.")