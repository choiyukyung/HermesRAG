from faiss import FaissIndexing

if __name__ == "__main__":
    loader = FaissIndexing()
    vectors = loader.load_vectors_from_db()

    # 로드된 벡터 확인
    if vectors:
        print(f"Successfully loaded {len(vectors)} vectors.")
    else:
        print("No vectors found.")