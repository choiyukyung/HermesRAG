import os

def get_project_root():
    # src 기준 상위 디렉토리
    current_path = os.path.abspath(".")
    while True:
        if os.path.isdir(current_path) and ".gradle" in os.listdir(current_path):
            return current_path # .gradle 폴더의 상위 폴더를 반환하도록 수정
        parent_path = os.path.dirname(current_path)
        if parent_path == current_path:
            return None  # 루트 디렉토리에 도달했지만 src 폴더를 찾지 못함
        current_path = parent_path
