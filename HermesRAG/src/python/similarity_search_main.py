import sys
import json
from similarity_search import SimilaritySearcher  # 검색 관련 코드가 있는 모듈

def main():
    if len(sys.argv) < 2:
        print("사용법: python search_articles.py <검색어>")
        return

    query = sys.argv[1]
    service = SimilaritySearcher()  # 검색 서비스 객체 생성
    results = service.search_articles(query, top_n=5)  # 검색 실행

    print(json.dumps(results, indent=4, ensure_ascii=False))  # JSON 형식으로 출력

if __name__ == "__main__":
    main()
