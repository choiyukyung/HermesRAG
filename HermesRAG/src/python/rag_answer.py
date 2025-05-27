import google.generativeai as genai
import os, json, sys
from similarity_search import SimilaritySearcher
from typing import List, Tuple, Dict, Any
import requests
from config import GUARDIAN_API_URL, GUARDIAN_API_KEY, QDRANT_SERVER_HOST, QDRANT_SERVER_PORT
from qdrant_client import QdrantClient


class Rag:
    def __init__(self, api_key: str):
        genai.configure(api_key=api_key)
        self.model = genai.GenerativeModel('gemini-2.0-flash-lite')


    def answer_to_ko(self, text: str, query: str) -> str:
        # 영어로 답변
        try:
            prompt = f"""user_question: {query}
            article_body: {text}
            
            Answer user_question in detail based on the article_body in Korean.
            within 4 lines without emphasis.
            Use this JSON schema:

            Answer = {{"answer": str}}
            Return: a single Answer object, not an array."""
            response = self.model.generate_content(
                prompt,
                generation_config={
                    "response_mime_type": "application/json"
                }
            )
            try:
                result = json.loads(response.text)
                return result.get("answer", "")
            except json.JSONDecodeError:
                return {"error": "Invalid JSON response from API"}

        except Exception as e:
            print(f"Error while answering: {e}")
            return ""

    def get_article_content(self, article_id):
        # 기사 본문 가져오기
        url = f'{GUARDIAN_API_URL}/{article_id}?api-key={GUARDIAN_API_KEY}&show-fields=body'

        # API 요청
        response = requests.get(url)

        if response.status_code == 200:
            article_data = response.json()
            # print(article_data['response']['content'])
            body_text = article_data['response']['content']['fields'].get('body', 'There is no body of article.')
            return body_text
        else:
            return f"Error fetching article: {response.status_code}"


    def answer_based_on_articles(self, articles: List[Dict[str, Any]], query: str) -> Dict[str, Any]:
        # 검색된 기사를 바탕으로 한국어 답변
        if not articles:
            return {"status": "error", "message": "No similar articles found"}

        # 가장 유사한 기사 내용 추출
        article_body = self.get_article_content(articles[0]['id'])
        answer = self.answer_to_ko(article_body, query)

        return {
            "status": "success",
            "message": answer,
            "articles": articles
        }


# if __name__ == "__main__":
#
#     #API_KEY 환경 변수로 저장
#     API_KEY = os.getenv('GOOGLE_API_KEY')
#
#     if not API_KEY:
#         print("Error: GOOGLE_API_KEY environment variable not set.")
#         sys.exit(1)
#
#     if len(sys.argv) < 2:
#         print("사용법: python search_articles.py <검색어>")
#     else:
#         client = QdrantClient(host=QDRANT_SERVER_HOST, port=QDRANT_SERVER_PORT)
#         searcher = SimilaritySearcher(client)
#         rag = Rag(API_KEY)
#
#         query = sys.argv[1]
#         similar_articles = searcher.find_similar_articles(query, top_n=5)
#         result = rag.answer_based_on_articles(similar_articles, query)
#         print(json.dumps(result))