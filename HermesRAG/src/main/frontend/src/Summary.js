import { useState } from "react";

const CommandSummary = () => {
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
    <div style={{ padding: "20px", textAlign: "center" }}>
      <h2>뉴스 요약 검색</h2>
      <input
        type="text"
        value={command}
        onChange={(e) => setCommand(e.target.value)}
        placeholder="검색어를 입력하세요..."
        style={{ padding: "10px", width: "300px", marginRight: "10px" }}
      />
      <button onClick={fetchSummary} style={{ padding: "10px 20px" }}>
        검색
      </button>

      {loading && <p>요약 중...</p>}

      {responseData && (
        <div style={{ marginTop: "20px", textAlign: "left", maxWidth: "800px", margin: "auto" }}>
          <h3>관련 뉴스 기사 요약:</h3>
          {responseData.articles && responseData.articles.length > 0 ? (
            <ul>
              {responseData.articles.map((article, index) => (
                <li key={index} style={{ marginBottom: "10px" }}>
                  <a href={article.web_url} target="_blank"><strong>{article.web_title}</strong></a> <br />
                  요약:<em> {article.korean_summary}</em> <br />
                  <small>유사도: {article.similarity.toFixed(4)}</small>
                </li>
              ))}
            </ul>
          ) : (
            <p>관련 뉴스가 없습니다.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default CommandSummary;
