from sentence_transformers import SentenceTransformer
from typing import List, Tuple, Dict, Any
from faiss_index import FaissIndexer
import sys

class SimilaritySearcher:
    def __init__(self, indexer: FaissIndexer, model_name: str = 'paraphrase-multilingual-mpnet-base-v2'):
        #  model_name: SentenceTransformer
        self.model = SentenceTransformer(model_name)
        self.index = indexer.get_index()
        self.articles = indexer.get_articles()

    def find_similar_articles(self, query: str, top_n: int = 5) -> List[Tuple[int, float]]:
        # 쿼리와 가장 유사한 기사 찾기

        # 쿼리 벡터화
        query_vector = self.model.encode(query).astype("float32").reshape(1, -1)

        distances, indices = self.index.search(query_vector, top_n)

        results = []
        for i in range(top_n):
            similarity = 1 / (1 + distances[0][i])  # L2 거리를 0~1 사이의 유사도로 변환
            idx = indices[0][i]
            article = self.articles[idx]

            results.append({
                "id": article["id"],
                "similarity": round(float(similarity), 4),
                "web_title": article["webTitle"],
                "trail_text": article["trailText"],
                "web_url": article["webUrl"]
            })

        return results

# if __name__ == "__main__":
#
#     if len(sys.argv) < 2:
#         print("사용법: python search_articles.py <검색어>")
#     else:
#         indexer = FaissIndexer()
#         searcher = SimilaritySearcher(indexer)
#
#         query = sys.argv[1]
#         similar_articles = searcher.find_similar_articles(query, top_n=5)
#
#         print("검색 결과:", similar_articles)