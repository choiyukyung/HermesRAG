from sentence_transformers import SentenceTransformer
from typing import List, Tuple, Dict, Any
from qdrant_client import QdrantClient

class SimilaritySearcher:
    def __init__(self, client: QdrantClient, collection_name: str = 'articles', model_name: str = 'paraphrase-multilingual-mpnet-base-v2'):
        self.model = SentenceTransformer(model_name)
        self.client = client
        self.collection_name = collection_name

    def find_similar_articles(self, query: str, top_n: int = 5) -> List[Tuple[int, float]]:
        # 쿼리 벡터화
        query_vector = self.model.encode(query).astype("float32").tolist()

        # Qdrant에서 유사도 검색
        search_result = self.client.search(
            collection_name=self.collection_name,
            query_vector=query_vector,
            limit=top_n,
        )

        results = []
        for result in search_result:
            similarity = 1 / (1 + result.score)  # L2 거리를 0~1 사이의 유사도로 변환

            results.append({
                "id": result.id,
                "similarity": round(float(similarity), 4),
                "web_title": result.payload.get("web_title", ""),
                "trail_text": result.payload.get("trail_text", ""),
                "web_url": result.payload.get("web_url", "")
            })

        return results
