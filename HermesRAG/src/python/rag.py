import google.generativeai as genai
import os, json, sys
from similarity_search import SimilaritySearcher
from typing import List, Tuple, Dict, Any

class Rag:
    def __init__(self, api_key: str):
        genai.configure(api_key=api_key)
        self.model = genai.GenerativeModel('gemini-2.0-flash-lite')

    def select_top_3_articles(self, articles: List[Dict[str, Any]], query: str) -> List[Dict[str, Any]]:
        prompt = f"사용자 질문: {query}\n\n기사 요약문:\n"
        for article in articles:
            prompt += f"{article['id']}. {article['trail_text']}\n"
        prompt += """
        위 기사 요약문 중에서 사용자 질문에 가장 관련성이 높은 3개의 id를 JSON 형식으로 나열하세요.
        Use this JSON schema:

        Article = {'selected_id': str}
        Return: list[Article]"""

        response = self.model.generate_content(
            prompt,
            generation_config={
                "response_mime_type": "application/json"
            }
        )

        try:
            result = json.loads(response.text)
        except json.JSONDecodeError:
            return {"error": "Invalid JSON response from API"}

        selected_ids = [article['selected_id'] for article in result]

        selected_articles = self.get_selected_articles(articles, selected_ids)
        return selected_articles

    def get_selected_articles(self, articles, selected_ids):
        selected_articles = []
        for article in articles:
            if article["id"] in selected_ids:
                selected_articles.append(article)
        return selected_articles


if __name__ == "__main__":
    #API_KEY 환경 변수로 저장
    API_KEY = os.getenv('GOOGLE_API_KEY')

    if not API_KEY:
        print("Error: GOOGLE_API_KEY environment variable not set.")
        sys.exit(1)

    query = sys.argv[1]
    searcher = SimilaritySearcher()
    rag = Rag(API_KEY)

    articles = searcher.search_articles(query, top_n=5)
    articlesTop3 = rag.select_top_3_articles(articles, query)
    print(json.dumps(articlesTop3))