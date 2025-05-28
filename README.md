# HermesRAG
RAG 기반 최신 기술과학 기사 제공/답변 서비스

### Development Period
2025.02 ~ 2025.03
### Developer
@choiyukyung, 개인 프로젝트
### 기술스택
- Backend - Spring Boot, MySQL
- AI (RAG) - Python(model: sentence-BERT, Gemini), FAISS, Qdrant
- Frontend - React.js

## Goal
HermesRAG 프로젝트는 **최신 뉴스**를 바탕으로 기술과학(+비지니스) 정보를 제공하는 서비스입니다.
외부 뉴스 API를 활용하여 주기적으로 데이터를 수집하고, RAG 기술을 통해 사용자가 원하는 핵심 내용을 추출하여 제공합니다.

## Key Features
1. 키워드 기반 최신 뉴스 검색 및 요약
- 유사도 검색을 이용해서 검색된 기사를 AI 모델에 제공하고,  AI 모델로 기사를 2차 선별하고 한국어 요약을 생성합니다.

![image](https://github.com/user-attachments/assets/8bc5a878-c5fc-4c49-88df-af1ed08d98b7) | ![image](https://github.com/user-attachments/assets/387551df-c145-4a9c-8a2e-9cdc36cbcd6f)
-- | -- |

2. 질문 기반 최신 답변 생성
- 유사도 검색을 이용하여 검색된 기사를 AI 모델에 제공하고, AI 모델로 사용자의 질문에 대한 답변을 생성합니다.

![image](https://github.com/user-attachments/assets/f383499b-aea0-4ef0-92d7-b56314051402) | ![image](https://github.com/user-attachments/assets/1d6746c1-317a-4f9f-a94b-19043fec279f)
-- | -- |


기사 제목 클릭 시 기사링크로 접속됩니다.

## Technologies Used
1. 벡터화
   - 뉴스 기사를 벡터화하여 벡터 데이터베이스에 저장해서 유사도 검색이 쉽도록 합니다.
2. 유사도 검색
   - 사용자 질의가 들어오면 같은 모델로 벡터화하여 코사인 유사도를 계산합니다. 키워드 검색과 달리 유사한 내용도 검색이 가능합니다.
3. RAG(Retrieval-Augmented Generation)
   - RAG는 정보 검색과 콘텐츠 생성을 통합한 기술입니다. 유사도 검색을 통해 정보를 제공하고, 검색된 기사들을 바탕으로 원하는 응답을 생성합니다.
4. 자동화
   - @Scheduled를 이용하여 주기적으로 최신 기사를 가져오고 벡터화하여 저장하도록 하였습니다.
5. 필요한 모델
   - 위 과정에서 필요한 모델은 파이썬을 사용하여 로드됩니다. 벡터화+유사도 검색 모델과, 응답 생성 모델은 비용과 정확도를 고려하여 결정했습니다.
  
## Simple Spring Boot - AI Diagram
<div align="center">
   <img src="https://github.com/user-attachments/assets/5108f724-70be-43ab-8d2f-4a6626958235" width="70%" />
</div>

