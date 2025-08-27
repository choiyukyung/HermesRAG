FROM python:3.11

WORKDIR /app

# torch CPU용 (사전 설치)
RUN pip install --no-cache-dir torch==2.3.1+cpu -f https://download.pytorch.org/whl/torch_stable.html

COPY requirements.txt .

#이 단계는 requirements.txt가 바뀌지 않으면 캐시됨!
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["uvicorn", "FastAPI:app", "--host", "0.0.0.0", "--port", "8000"]
