# HermesRAG
HermesRAG project for Spring Boot
### Development Period
2025.02 ~ 2025.03
### Developer
최유경, 개인 프로젝트

## Goal
HermesRAG 프로젝트는 사용자가 원하는 기술과학 정보를 **최신 뉴스**를 바탕으로 빠르게 제공하는 서비스를 목표로 합니다. 외부 뉴스 API를 활용하여 주기적으로 데이터를 수집하고, RAG 기술을 통해 뉴스의 핵심 내용을 추출하여 제공합니다.

## Key Features
1. 키워드 기반 최신 뉴스 검색 및 요약

![image](https://github.com/user-attachments/assets/8bc5a878-c5fc-4c49-88df-af1ed08d98b7) | ![image](https://github.com/user-attachments/assets/387551df-c145-4a9c-8a2e-9cdc36cbcd6f)
-- | -- |

2. 질문 기반 최신 답변 생성

![image](https://github.com/user-attachments/assets/f383499b-aea0-4ef0-92d7-b56314051402) | ![image](https://github.com/user-attachments/assets/1d6746c1-317a-4f9f-a94b-19043fec279f)
-- | -- |

## Technologies Used
1. 벡터화
   - 뉴스 가사를 벡터화하여 수치적인 형태로 변환해서 유사도 검색이 쉽도록 합니다. 
2. 유사도 검색
   - 사용자 질의가 들어오면 같은 모델로 벡터화하여 코사인 유사도를 계산합니다. 키워드 검색과 달리 유사한 내용도 검색이 가능합니다.
3. RAG(Recency-Augmented Generation)
   - RAG는 정보 검색과 콘텐츠 생성을 통합한 기술입니다. 유사도 검색을 통해 정보를 제공하고, 검색된 기사들을 바탕으로 원하는 응답을 생성합니다.
4. 자동화
   - @Scheduled를 이용하여 주기적으로 최신 기사를 가져오고 벡터화하여 저장하도록 하였습니다.
5. 필요한 모델
   - 위 과정에서 필요한 모델은 파이썬을 사용하여 로드됩니다. 벡터화+유사도 검색 모델과, 응답 생성 모델은 비용과 정확도를 고려하여 결정했습니다.
