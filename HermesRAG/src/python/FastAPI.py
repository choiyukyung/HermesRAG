from fastapi import FastAPI

from vectorize import ArticleVectorizer
from similarity_search import SimilaritySearcher
from rag_answer import Rag
from rag_summary import Rag

from qdrant_client import QdrantClient
from config import API_URL_VECTORIZE, QDRANT_SERVER_HOST, QDRANT_SERVER_PORT
import os, json, sys

app = FastAPI()

@app.get("/run-vectorize")
def run_vectorize():
    vectorizer = ArticleVectorizer()
    result = vectorizer.process_article(API_URL_VECTORIZE)

    return result


@app.get("/run-rag-answer")
def run_rag_answer():

    #API_KEY 환경 변수로 저장
    API_KEY = os.getenv('GOOGLE_API_KEY')

    if not API_KEY:
        return "Error: GOOGLE_API_KEY environment variable not set."

    if len(sys.argv) < 2:
        return "사용법: python search_articles.py <검색어>"
    else:
        client = QdrantClient(host=QDRANT_SERVER_HOST, port=QDRANT_SERVER_PORT)
        searcher = SimilaritySearcher(client)
        rag = Rag(API_KEY)

        query = sys.argv[1]
        similar_articles = searcher.find_similar_articles(query, top_n=5)
        result = rag.answer_based_on_articles(similar_articles, query)

        return result


@app.get("/run-rag-summary")
def run_rag_summary():
    #API_KEY 환경 변수로 저장
    API_KEY = os.getenv('GOOGLE_API_KEY')

    if not API_KEY:
        return "Error: GOOGLE_API_KEY environment variable not set."

    if len(sys.argv) < 2:
        return "사용법: python search_articles.py <검색어>"
    else:
        client = QdrantClient(host=QDRANT_SERVER_HOST, port=QDRANT_SERVER_PORT)
        searcher = SimilaritySearcher(client)
        rag = Rag(API_KEY)

        query = sys.argv[1]
        similar_articles = searcher.find_similar_articles(query, top_n=5)
        articlesTop3 = rag.select_top_3_articles(similar_articles, query)
        result = rag.summarize_articles(articlesTop3)

        return result