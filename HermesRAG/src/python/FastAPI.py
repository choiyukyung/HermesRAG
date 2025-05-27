from fastapi import FastAPI
from vectorize import ArticleVectorizer
from config import API_URL_VECTORIZE

app = FastAPI()

@app.get("/run-vectorize")
def run_vectorize():
    vectorizer = ArticleVectorizer()
    result = vectorizer.process_article(API_URL_VECTORIZE)

    return result
