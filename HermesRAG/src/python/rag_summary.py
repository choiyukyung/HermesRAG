import google.generativeai as genai
import json
from typing import List, Tuple, Dict, Any

class Rag:
    def __init__(self, api_key: str):
        genai.configure(api_key=api_key)
        self.model = genai.GenerativeModel('gemini-2.0-flash-lite')


    def summarize_en_to_ko(self, text: str) -> str:
        # 영어 텍스트 요약
        try:
            prompt = f"""Summarize the following text in Korean, in JSON format: {text}
            Ensure the summary ends with a complete sentence.
            Use this JSON schema:

            Summary = {{"korean_summary": str}}
            Return: a single Summary object, not an array."""
            response = self.model.generate_content(
                prompt,
                generation_config={
                    "response_mime_type": "application/json"
                }
            )
            try:
                result = json.loads(response.text)
                return result.get("korean_summary", "")
            except json.JSONDecodeError:
                return {"error": "Invalid JSON response from API"}
        except Exception as e:
            print(f"Error while summarizing: {e}")
            return ""


    def summarize_articles(self, articles: List[Dict[str, Any]]) -> Dict[str, Any]:
        # 검색된 기사 한국어 요약
        if not articles:
            return {"status": "error", "message": "No similar articles found"}

        for article in articles:
            article_text = article.get('trail_text', '')
            if article_text:
                article['korean_summary'] = self.summarize_en_to_ko(article_text)
            else:
                article['korean_summary'] = "No summary available."


        return {
            "status": "success",
            "message": "Articles summarized successfully",
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
#         articlesTop3 = rag.select_top_3_articles(similar_articles, query)
#         result = rag.summarize_articles(articlesTop3)
#         print(json.dumps(result))

