import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';

const HomeWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 50px;
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
  font-size: 30px;
  font-weight: bold;
`;

const Home = () => {
  const navigate = useNavigate();

    const goToRagPage = () => {
      navigate('/rag');
    };

  return (
    <HomeWrapper>
      <LogoContainer>
        <Logo src="/logo192.png" alt="HermesRAG Logo" />
        <TitleText>HermesRAG</TitleText>
      </LogoContainer>
      <div>AI 기반 뉴스 요약 및 검색 서비스</div>
      <div>
        HermesRAG는 최신 뉴스를 AI 기반으로 분석하여 <b>맞춤형 요약</b>을 제공합니다.
      </div>
      <div>
        원하는 키워드로 <b>관련 뉴스</b>를 검색하고, 빠르게 핵심 내용을 확인하세요!
      </div>
      <button onClick={goToRagPage}>요약하기</button>
    </HomeWrapper>
  );
};

export default Home;
