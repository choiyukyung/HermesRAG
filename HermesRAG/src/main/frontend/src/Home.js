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

    const goToSummaryPage = () => {
      navigate('/summary');
    };

  return (
    <HomeWrapper>
      <LogoContainer>
        <Logo src="/logo192.png" alt="HermesRAG Logo" />
        <TitleText>HermesRAG</TitleText>
      </LogoContainer>
      <div>AI 기반 뉴스 요약 및 검색 서비스</div>
      <div>
        HermesRAG는 사용자의 검색어와 가장 관련성이 높은 최신 기사 3개를 찾아 요약하여 제공합니다.
      </div>
      <div>
         또한, 최신 기사 내용을 기반으로 사용자의 질문에 대한 답변도 제공합니다.
      </div>
      <button onClick={goToSummaryPage}>요약하기</button>
    </HomeWrapper>
  );
};

export default Home;
