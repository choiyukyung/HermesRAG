from find_root import get_project_root
import os

DB_CONFIG = {
    'host': 'localhost',
    'user': 'host',
    'password': '1234',
    'database': 'hermesdb'
}

API_URL_VECTORIZE = "http://localhost:8080/api/vectorize/get"
API_URL_SEARCH = "http://localhost:8080/api/search/get"
API_URL_FAISS = "http://localhost:8080/api/faiss/get"

GUARDIAN_API_URL="https://content.guardianapis.com"
GUARDIAN_API_KEY="fec93f93-a9c4-4c9b-9386-f7697f23e090"

PROJECT_ROOT = get_project_root()
QDRANT_DATA_PATH = os.path.join(PROJECT_ROOT, 'src', 'python', 'qdrant_data')