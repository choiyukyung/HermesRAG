FROM python:3.11

WORKDIR /app

COPY requirements.txt .

#이 단계는 requirements.txt가 바뀌지 않으면 캐시됨!
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["uvicorn", "FastAPI:app", "--host", "0.0.0.0", "--port", "8000"]
