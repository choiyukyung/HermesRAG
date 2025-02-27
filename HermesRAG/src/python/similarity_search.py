import numpy as np
from sentence_transformers import SentenceTransformer
import mysql.connector
from config import DB_CONFIG, API_URL_SEARCH
from typing import List, Tuple, Dict, Any
import requests
import base64

class SimilaritySearcher:
    def __init__(self, model_name: str = 'paraphrase-multilingual-mpnet-base-v2'):
        #  model_name: SentenceTransformer
        self.model = SentenceTransformer(model_name)
        self.db_config = DB_CONFIG

    def get_article_vectors(self, api_url_vector: str) -> List[Tuple[str, np.ndarray, np.ndarray]]:
        # Spring Boot API에서 기사의 벡터 가져오기
        response = requests.get(api_url_vector)
        vector_data = response.json()

        results = []

        for item in vector_data:
            article_id = item['id']

            # Base64로 인코딩된 문자열을 바이트로 변환
            web_title_embedding_bytes = base64.b64decode(item['webTitleEmbedding'])
            trail_text_embedding_bytes = base64.b64decode(item['trailTextEmbedding'])

            # BLOB 데이터를 NumPy 배열로 변환
            web_title_vector = np.frombuffer(web_title_embedding_bytes, dtype=np.float32)
            trail_text_vector = np.frombuffer(trail_text_embedding_bytes, dtype=np.float32)

            results.append((article_id, web_title_vector, trail_text_vector))

        return results


    def cosine_similarity(self, vec1: np.ndarray, vec2: np.ndarray) -> float:
        """두 벡터 간의 코사인 유사도 계산"""
        return np.dot(vec1, vec2) / (np.linalg.norm(vec1) * np.linalg.norm(vec2))

    def find_similar_articles(self, query: str, top_n: int = 10, use_web_title: bool = True, similarity_threshold: float = 0.4) -> List[Tuple[int, float]]:
        """
        쿼리와 가장 유사한 기사 찾기
        Args:
            query: 검색 쿼리
            top_n: 반환할 결과 수 (기본값: 10)
            use_web_title: True면 제목 벡터 사용, False면 본문 요약 벡터 사용
            similarity_threshold: 유사도 임계값 (기본값: 0.4)
        Returns:
            [(article_id, similarity_score), ...] 형태의 결과 리스트
        """
        # 쿼리 벡터화
        query_vector = self.model.encode(query)

        # 한 달간의 기사 벡터 가져오기
        all_articles = self.get_article_vectors(API_URL_SEARCH)
        similarities = []

        for article_id, web_title_vector, trail_text_vector in all_articles:
            # 제목 또는 본문 요약 벡터 선택
            article_vector = web_title_vector if use_web_title else trail_text_vector

            # 벡터 차원이 다른 경우 조정
            if len(article_vector) != len(query_vector):
                # 모델의 출력 차원에 맞게 조정
                article_vector = article_vector[:len(query_vector)] if len(article_vector) > len(query_vector) else np.pad(article_vector, (0, len(query_vector) - len(article_vector)))

            # 코사인 유사도 계산
            similarity = self.cosine_similarity(query_vector, article_vector)
            # 유사도가 임계값 이상인 경우에만 추가
            if similarity >= similarity_threshold:
                similarities.append((article_id, similarity))

        # 유사도 기준 내림차순 정렬 후 상위 N개 반환
        return sorted(similarities, key=lambda x: x[1], reverse=True)[:top_n]

    def get_article_details(self, article_ids: List[int]) -> List[dict]:
        """기사 ID 리스트를 받아 기사 상세 정보 반환"""
        conn = mysql.connector.connect(**self.db_config)
        cursor = conn.cursor(dictionary=True)

        try:
            # 전달된 ID 목록으로 IN 쿼리 생성
            placeholders = ', '.join(['%s'] * len(article_ids))
            query = f"""
                SELECT id, web_title, trail_text
                FROM article
                WHERE id IN ({placeholders})
                ORDER BY FIELD(id, {placeholders})
            """
            # 파라미터 두 번 전달 (IN 절과 ORDER BY FIELD 절)
            cursor.execute(query, article_ids + article_ids)
            return cursor.fetchall()

        finally:
            cursor.close()
            conn.close()

    def search_articles(self, query: str, top_n: int = 5, similarity_threshold: float = 0.3) -> List[Dict[str, Any]]:
        """유사 기사 검색 후 상세 정보 반환"""
        similar_articles = self.find_similar_articles(query, top_n, similarity_threshold=similarity_threshold)

        if not similar_articles:
            return []

        article_ids = [article_id for article_id, _ in similar_articles]
        articles = self.get_article_details(article_ids)

        for article, (_, similarity) in zip(articles, similar_articles):
            article["similarity"] = float(np.float32(similarity))

        return articles
