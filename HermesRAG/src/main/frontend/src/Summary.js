import { useState } from "react";
import styled from 'styled-components';

const SummaryWrapper = styled.div`
  padding: 20px;
  text-align: center;
  height: 100vh;
  background-color: #fff8f1;
`;

const SearchInput = styled.input`
  padding: 10px;
  width: 300px;
  margin-right: 10px;
  border: 2px solid #ffa500;
  border-radius: 5px;
`;

const SearchButton = styled.button`
  padding: 10px 20px;
  background-color: #ffa500;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  &:hover {
    background-color: #e67e22;
  }
`;

const ResultContainer = styled.div`
  margin-top: 20px;
  text-align: left;
  max-width: 1000px;
  margin: auto;
`;

const ArticleList = styled.ul`
  list-style-type: none;
  padding-left: 0;
`;

const ArticleItem = styled.li`
  margin-bottom: 10px;
`;

const ArticleLink = styled.a`
  font-weight: bold;
  font-size: 28px;
  color: #ffa500;
  text-decoration: none;
  &:hover {
    text-decoration: underline;
  }
`;

const ArticleSummary = styled.em`
  font-style: italic;
`;

const Similarity = styled.small`
  color: #888;
`;

const Dissatisfaction = styled.em`
  color: #555;
  font-size: 12px;
`;


const Summary = () => {
  const [command, setCommand] = useState(""); // 입력된 명령어
  const [responseData, setResponseData] = useState(null); // API 응답 저장
  const [loading, setLoading] = useState(false); // 로딩 상태

  const fetchSummary = async () => {
    if (!command.trim()) return; // 빈 입력 방지
    setLoading(true);
    setResponseData(null); // 기존 결과 초기화

    try {
      const formData = new FormData();
      formData.append("query", command);

      const response = await fetch("/api/rag/summarize", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) throw new Error("서버 응답 오류");

      const data = await response.json();
      setResponseData(data); // 응답 저장
    } catch (error) {
      console.error("Error fetching summary:", error);
      setResponseData({ message: "요약을 불러오는 중 오류가 발생했습니다." });
    } finally {
      setLoading(false);
    }
  };

  return (
    <SummaryWrapper>
      <h2>최신 기술과학 뉴스 요약</h2>
      <SearchInput
        type="text"
        value={command}
        onChange={(e) => setCommand(e.target.value)}
        placeholder="키워드를 입력하세요..."
      />
      <SearchButton onClick={fetchSummary}>검색</SearchButton>

      {loading && <p>요약 중...</p>}

      {responseData && (
        <ResultContainer>
          <h3>관련 뉴스 기사 요약:</h3>
          {responseData.articles && responseData.articles.length > 0 ? (
            <ArticleList>
              {responseData.articles.map((article, index) => (
                <ArticleItem key={index}>
                  <ArticleLink href={article.web_url} target="_blank">
                    {article.web_title}
                  </ArticleLink> <br />
                  요약: <ArticleSummary>{article.korean_summary}</ArticleSummary> <br />
                  <Similarity>유사도: {article.similarity.toFixed(4)}</Similarity>
                </ArticleItem>
              ))}
            </ArticleList>
          ) : (
            <p>관련 뉴스가 없습니다.</p>
          )}
          <Dissatisfaction><div>만족스러운 결과를 얻지 못하셨다면, 조금 더 자세히 검색해보세요.</div></Dissatisfaction>
        </ResultContainer>
      )}
    </SummaryWrapper>
  );
};

export default Summary;
