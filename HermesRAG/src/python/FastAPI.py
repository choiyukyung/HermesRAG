from fastapi import FastAPI, Form
from qdrant_client import QdrantClient

from vectorize import ArticleVectorizer
from similarity_search import SimilaritySearcher
from rag_answer import Rag as RagAnswer
from rag_summary import Rag as RagSummary

from config import API_URL_VECTORIZE, QDRANT_SERVER_HOST, QDRANT_SERVER_PORT
import os, json

app = FastAPI()

@app.get("/run-vectorize")
def run_vectorize():
    vectorizer = ArticleVectorizer()
    result = vectorizer.process_article(API_URL_VECTORIZE)

    return result

@app.post("/run-rag-summary")
def run_rag_summary(query: str = Form(...)):

    API_KEY = os.getenv('GOOGLE_API_KEY')
    if not API_KEY:
        return "Error: GOOGLE_API_KEY environment variable not set."

    client = QdrantClient(host=QDRANT_SERVER_HOST, port=QDRANT_SERVER_PORT)
    searcher = SimilaritySearcher(client)
    rag = RagSummary(API_KEY)

    similar_articles = searcher.find_similar_articles(query, top_n=5)
    result = rag.summarize_articles(similar_articles)

    return result

@app.post("/run-rag-answer")
def run_rag_answer(query: str = Form(...)):

    API_KEY = os.getenv('GOOGLE_API_KEY')
    if not API_KEY:
        return "Error: GOOGLE_API_KEY environment variable not set."

    try:
        client = QdrantClient(host=QDRANT_SERVER_HOST, port=QDRANT_SERVER_PORT)
        searcher = SimilaritySearcher(client)
        rag = RagAnswer(API_KEY)

        similar_articles = searcher.find_similar_articles(query, top_n=5)
        result = rag.answer_based_on_articles(similar_articles, query)

        return result

    except Exception as e:
        import traceback
        traceback.print_exc()
        return {"error": str(e)}