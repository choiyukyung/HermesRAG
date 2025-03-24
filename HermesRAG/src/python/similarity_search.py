from sentence_transformers import SentenceTransformer
from typing import List, Tuple, Dict, Any
from qdrant_client import QdrantClient, models
from datetime import datetime, timedelta

class SimilaritySearcher:
    def __init__(self, client: QdrantClient, collection_name: str = 'articles', model_name: str = 'paraphrase-multilingual-mpnet-base-v2'):
        self.model = SentenceTransformer(model_name)
        self.client = client
        self.collection_name = collection_name

    def find_similar_articles(self, query: str, top_n: int = 5) -> List[Tuple[int, float]]:
        # 쿼리 벡터화
        query_vector = self.model.encode(query).astype("float32").tolist()

        # 최근 한 달
        one_month_ago = datetime.now() - timedelta(days=30)
        one_month_ago_timestamp = int(one_month_ago.timestamp()) # LocalDateTime 형식

        # Qdrant에서 유사도 검색
        search_result = self.client.query_points(
            collection_name=self.collection_name,
            query=query_vector,
            limit=top_n,
            query_filter=models.Filter(
                must=[
                    models.FieldCondition( # 날짜 필터링: 한 달 이내의 데이터만 검색
                        key="web_publication_date",
                        range=models.DatetimeRange(
                            gte=one_month_ago_timestamp,
                            lte=int(datetime.now().timestamp())
                        ),
                    )
                ]
            ),
            with_payload=True
        )

        results = []
        for ScoredPoint in search_result.points:
            similarity = 1 / (1 + ScoredPoint.score) # L2 거리를 0~1 사이의 유사도로 변환

            results.append({
                "id": ScoredPoint.payload.get("article_id", ""),
                "similarity": round(float(similarity), 4),
                "web_title": ScoredPoint.payload.get("web_title", ""),
                "trail_text": ScoredPoint.payload.get("trail_text", ""),
                "web_url": ScoredPoint.payload.get("web_url", "")
            })

        return results
