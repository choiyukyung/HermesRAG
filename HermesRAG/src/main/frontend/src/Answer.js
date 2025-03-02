import { useState } from "react";

const CommandSummary = () => {
  const [command, setCommand] = useState("");
  const [responseData, setResponseData] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchSummary = async () => {
    if (!command.trim()) return;
    setLoading(true);
    setResponseData(null);

    try {
      const formData = new FormData();
      formData.append("query", command);

      const response = await fetch("/api/rag/answer", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) throw new Error("서버 응답 오류");

      const data = await response.json();
      setResponseData(data);
    } catch (error) {
      console.error("Error fetching answer:", error);
      setResponseData({ message: "답변을 불러오는 중 오류가 발생했습니다." });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "20px", textAlign: "center" }}>
      <h2>최신 뉴스 바탕 답변</h2>
      <input
        type="text"
        value={command}
        onChange={(e) => setCommand(e.target.value)}
        placeholder="질문을 입력하세요..."
        style={{ padding: "10px", width: "300px", marginRight: "10px" }}
      />
      <button onClick={fetchSummary} style={{ padding: "10px 20px" }}>
        검색
      </button>

      {loading && <p>답변 준비 중...</p>}

      {responseData && (
        <div style={{ marginTop: "20px", textAlign: "left", maxWidth: "800px", margin: "auto" }}>
          <h3>관련 뉴스 기사 답변:</h3>
          {responseData.message && (
            <p><strong>AI 답변: </strong> {responseData.message}</p>
          )}
          <strong>관련 기사: </strong>
          {responseData.articles && responseData.articles.length > 0 ? (
            <ul>
              {responseData.articles.map((article, index) => (
                <li style={{ marginBottom: "10px" }}>
                  <a href={article.web_url} target="_blank"><strong>{article.web_title}</strong></a> <br />
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
