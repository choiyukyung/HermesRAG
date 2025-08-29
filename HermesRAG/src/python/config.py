import os
from dotenv import load_dotenv

load_dotenv()  # .env 파일 로드

# Google API
GOOGLE_API_KEY = os.getenv("GOOGLE_API_KEY")

# Guardian API
GUARDIAN_API_KEY = os.getenv("GUARDIAN_API_KEY")
GUARDIAN_API_URL = os.getenv("GUARDIAN_API_URL")


DB_CONFIG = {
    'host': 'mysqldb',
    'user': 'user',
    'password': '1256',
    'database': 'hermesrag'
}

API_URL_VECTORIZE = "http://springboot:8080/api/vectorize/get"
API_URL_SEARCH = "http://springboot:8080/api/search/get"
API_URL_FAISS = "http://springboot:8080/api/faiss/get"

QDRANT_SERVER_HOST = "qdrant"
QDRANT_SERVER_PORT = 6333