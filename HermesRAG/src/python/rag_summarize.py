import sys
from typing import Dict, Any, List
from transformers import T5ForConditionalGeneration, T5Tokenizer, MarianMTModel, MarianTokenizer
from similarity_search import SimilaritySearcher
import json

class RagSummarizer:
    def __init__(self):
        # 요약 모델 로드
        self.t5_model_name = "t5-small"
        self.t5_tokenizer = T5Tokenizer.from_pretrained(self.t5_model_name)
        self.t5_model = T5ForConditionalGeneration.from_pretrained(self.t5_model_name)

    def summarize_english_text(self, text: str) -> str:
        """영어 텍스트 요약"""
        try:
            input_text = f"Please summarize the following text: {text}"
            input_ids = self.t5_tokenizer(input_text, return_tensors="pt", max_length=1024, truncation=True).input_ids
            summary_ids = self.t5_model.generate(
                input_ids,
                max_length=150,
                min_length=40,
                num_beams=4,
                early_stopping=True
            )
            return self.t5_tokenizer.decode(summary_ids[0], skip_special_tokens=True)
        except Exception as e:
            print(f"Error while summarizing: {e}")
            return ""


    def summarize_articles(self, articles: List[Dict[str, Any]]) -> Dict[str, Any]:
        """검색된 기사 요약"""
        if not articles:
            return {"status": "error", "message": "No similar articles found"}

        combined_text = " ".join([article.get('trail_text', '') for article in articles]).strip()
        if not combined_text:
            return {"status": "error", "message": "There is no body of article."}

        english_summary = self.summarize_english_text(combined_text)

        return {
            "status": "success",
            "articles": articles,
            "english_summary": english_summary
        }


if __name__ == "__main__":
    query = sys.argv[1]
    searcher = SimilaritySearcher()
    summarizer = RagSummarizer()

    articles = searcher.search_articles(query, top_n=5)
    result = summarizer.summarize_articles(articles)

    print(json.dumps(result))