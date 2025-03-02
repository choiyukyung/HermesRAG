import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';

const HomeWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 50px;
  background-color: #fff8f1; // 배경색을 오렌지 계열로 부드럽게 설정
  height: 100vh;
  justify-content: center;
`;

const LogoContainer = styled.div`
  display: flex;
  align-items: center;
  padding-top: 20px;
  padding-bottom: 20px;
`;

const Logo = styled.img`
  width: 40px;
  height: 40px;
  margin-right: 10px;
`;

const TitleText = styled.div`
  font-size: 40px;
  font-weight: bold;
`;

const InfoText = styled.div`
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #555; // 회색
  text-align: center;
`;

const Box = styled.div`
  border: 2px solid #ffa500; // 오렌지색
  padding: 30px; // 박스 내부 여백
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 80%;
  max-width: 600px;
  margin-top: 20px;
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Button = styled.button`
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
  background-color: #ffa500;
  color: white; // 글자 색
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 15px;
  &:hover {
    background-color: #e67e22; //포인터 올리면 색 변경
  }
`;

const Home = () => {
  const navigate = useNavigate();

  const goToSummaryPage = () => {
    navigate('/summary');
  };

  const goToAnswerPage = () => {
    navigate('/answer');
  };

  return (
    <HomeWrapper>
      <LogoContainer>
        <Logo src="/logo192.png" alt="HermesRAG Logo" />
        <TitleText>HermesRAG</TitleText>
      </LogoContainer>
      <InfoText>AI 기반 뉴스 요약 및 검색 서비스</InfoText>

      <Box>
        <div>사용자의 검색어와 가장 관련성이 높은 최신 <strong>기술 과학 분야</strong> 기사 3개를 찾아 요약하여 제공합니다.</div>
        <Button onClick={goToSummaryPage}>요약하기</Button>
      </Box>

      <Box>
        <div>최신 <strong>기술 과학 분야</strong> 기사 내용을 기반으로 사용자의 질문에 대한 답변을 제공합니다.</div>
        <Button onClick={goToAnswerPage}>질문하기</Button>
      </Box>
    </HomeWrapper>
  );
};

export default Home;
