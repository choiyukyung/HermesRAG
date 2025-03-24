from qdrant_client import QdrantClient
from similarity_search import SimilaritySearcher

if __name__ == "__main__":
    # Qdrant 클라이언트 연결
    client = QdrantClient(path="qdrant_data")

    # SimilaritySearcher 객체 생성
    searcher = SimilaritySearcher(client)

    query = "AI 일자리"
    similar_articles = searcher.find_similar_articles(query, top_n=5)

    print("검색 결과:", similar_articles)
