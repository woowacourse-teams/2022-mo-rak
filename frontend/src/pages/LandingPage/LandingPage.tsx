import styled from '@emotion/styled';

import React, { RefObject, useEffect, useRef } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import {
  getLocalStorageItem,
  saveLocalStorageItem,
  removeLocalStorageItem,
  getSessionStorageItem,
  removeSessionStorageItem
} from '../../utils/storage';
import Logo from '../../assets/logo.svg';
import Circle from '../../assets/half-circle.svg';
import Smile from '../../assets/smile.svg';
import Line from '../../assets/line.svg';
import Glitter from '../../assets/glitter.svg';
import Highlight from '../../assets/highlight.svg';
import Poll from '../../assets/poll-small.svg';
import Appointment from '../../assets/time.svg';
import Undefined from '../../assets/question.svg';
import Services from '../../assets/service-group.svg';
import Blob from '../../assets/blob.svg';
import CircleHighlight from '../../assets/circle-highlight.svg';
import GithubLogo from '../../assets/github-logo.svg';
import { signin } from '../../api/auth';
import { getDefaultGroup } from '../../api/group';

// TODO: 페이지 추상화
function LandingPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const firstSection = useRef<HTMLTableSectionElement>(null);
  const secondSection = useRef<HTMLTableSectionElement>(null);
  const thirdSection = useRef<HTMLTableSectionElement>(null);

  const redirectUrl = getSessionStorageItem('redirectUrl');
  // TODO: 중복 로직해결
  // TODO: 다시 한 번 token을 가져오는
  const token = getLocalStorageItem('token');
  useEffect(() => {
    const fetchGetDefaultGroup = async () => {
      try {
        const { code: groupCode } = await getDefaultGroup();

        navigate(`/groups/${groupCode}`);
      } catch (err) {
        if (err instanceof Error) {
          const statusCode = err.message;
          if (statusCode === '401') {
            removeLocalStorageItem('token');
            navigate('/');

            return;
          }

          if (statusCode === '404') {
            navigate('/init');
            console.log('랜딩페이지에서 로그인을 했지만, 속해있는 그룹이 없습니다.');
          }
        }
      }
    };

    if (token) {
      fetchGetDefaultGroup();
    }
  }, []);

  useEffect(() => {
    // TODO: fetch하는 함수이지만 navigate도 해주고있다..근데 try..catch...사용하려면 이렇게밖에 못함
    // TODO: strict mode라서 로그인이 된 이후에도 요청을 다시 보내서 에러가 나온다.
    const fetchSignin = async (code: string) => {
      try {
        const { token } = await signin(code);

        saveLocalStorageItem<string>('token', token);

        if (redirectUrl) {
          navigate(redirectUrl);
          removeSessionStorageItem('redirectUrl');

          return;
        }

        navigate('/init');
      } catch (err) {
        alert('로그인에 실패하였습니다. 다시 시도해주세요');
        navigate('/');
      }
    };

    const code = searchParams.get('code');

    if (code) {
      fetchSignin(code);
    }
  }, []);

  const handleMoveToSection = (section: RefObject<HTMLTableSectionElement>) => () => {
    section.current?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <StyledContainer>
      <StyledMainSection ref={firstSection}>
        <StyledNavbar>
          <li onClick={handleMoveToSection(firstSection)}>LOGIN</li>
          <li onClick={handleMoveToSection(secondSection)}>ABOUT</li>
          <li onClick={handleMoveToSection(thirdSection)}>CONTACT US</li>
        </StyledNavbar>
        <StyledLogo src={Logo} alt="logo" />
        <StyledSmallTitle>
          모락이 해줄게요
        </StyledSmallTitle>
        <StyledBigTitle>
          <StyledSmileImage src={Smile} alt="smile" />
          <StyledLineImage src={Line} alt="line" />
          <StyledGlitterImage src={Glitter} alt="glitter" />
          모임을
          <br />
          즐겁게, 편하게!
        </StyledBigTitle>
        <StyledLink href="https://github.com/login/oauth/authorize?client_id=f67a30d27afefe8b241f">
          <StyledLoginContainer>
            <StyledGithubLogo src={GithubLogo} alt="github-logo" />
            <StyledLoginText>GITHUB으로 로그인</StyledLoginText>
          </StyledLoginContainer>
        </StyledLink>
        <StlyedSectionGuideContainer>
          <StlyedBlobContainer>
            <img src={Blob} alt="blob" />
            <StyledGuideText>모락 소개</StyledGuideText>
          </StlyedBlobContainer>
        </StlyedSectionGuideContainer>
      </StyledMainSection>

      <StyledIntroduceSection ref={secondSection}>
        <StyledNavbar>
          <li onClick={handleMoveToSection(firstSection)}>LOGIN</li>
          <li onClick={handleMoveToSection(secondSection)}>ABOUT</li>
          <li onClick={handleMoveToSection(thirdSection)}>CONTACT US</li>
        </StyledNavbar>
        <div>
          <StyledMainIntroduceText>
            분산된 서비스...
            <br />
            불편하지 않으셨나요?
            <StyledHighlightImage src={CircleHighlight} alt="highlight" />
          </StyledMainIntroduceText>
          <StyledDetailIntroduceText>
            투표 하기, 일정 정하기...
            분산 되어있는 서비스에 불편함을 느끼지 않으셨나요?
            <br />
            모락에 모두 모여있어요!
          </StyledDetailIntroduceText>
        </div>

        <StyledImageWrapper>
          <img src={Services} alt="services" />
        </StyledImageWrapper>
      </StyledIntroduceSection>

      <StyledServiceIntroduceSection ref={thirdSection}>
        <StyledNavbar>
          <li onClick={handleMoveToSection(firstSection)}>LOGIN</li>
          <li onClick={handleMoveToSection(secondSection)}>ABOUT</li>
          <li onClick={handleMoveToSection(thirdSection)}>CONTACT US</li>
        </StyledNavbar>
        <StyledExplanationTextContainer>
          <StyledExplanationBigText>
            모락을 통해서 할 수 있어요!
          </StyledExplanationBigText>
          <StyledExplanationSmallText>
            모락이 모임을 더 편하고, 즐겁게 할 수 있도록 도와줄게요
          </StyledExplanationSmallText>
        </StyledExplanationTextContainer>

        <StyledServiceContainer>
          <div>
            <StyledServiceCircle>
              <StyledPollImage src={Poll} alt="poll" />
              <StyledServieHighlightImage src={Highlight} alt="highlight" />
            </StyledServiceCircle>
            <StyledServiceName>투표하기</StyledServiceName>
          </div>

          <div>
            <StyledServiceCircle>
              <StyledAppointmentImage src={Appointment} alt="appointment" />
            </StyledServiceCircle>
            <StyledServiceName>약속잡기</StyledServiceName>
          </div>

          <div>
            <StyledServiceCircle>
              <StyledUndefinedImage src={Undefined} alt="undefined" />
              <StyledServieGlitterImage src={Glitter} alt="glitter" />
            </StyledServiceCircle>
            <StyledServiceName>coming soon!</StyledServiceName>
          </div>

          <div>
            <StyledServiceCircle>
              <StyledUndefinedImage src={Undefined} alt="undefined" />
            </StyledServiceCircle>
            <StyledServiceName>coming soon!</StyledServiceName>
          </div>
        </StyledServiceContainer>
      </StyledServiceIntroduceSection>

    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  scroll-snap-type: y mandatory;
  overflow-y: scroll;
  height: 100vh;
  width: 100%;
  font-family: 'Nanum Gothic', sans-serif;
`;

const StyledLogo = styled.img`
  width: 10em; 
  position: absolute; 
  top: 2rem; 
  left: 4rem;
`;

// section 1
// TODO: section태그마다 반복되는 속성들을 공통으로 묶어줄 수 있는 방법은 없을까
const StyledMainSection = styled.section`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  scroll-snap-align: start;
  position: relative;
  flex-direction: column;
  background-image: url(${Circle});
  background-size: contain;
  background-repeat: no-repeat;
  background-position: bottom 0px right 0px;
`;

const StyledNavbar = styled.nav`
  position: absolute;
  right: 12rem;
  top: 6rem;
  display: flex;
  list-style: none;
  gap: 4rem;
  font-size: 2rem;
  cursor: pointer;
`;

const StyledSmallTitle = styled.div`
  font-size: 2.4rem;
  margin-bottom: 1.2rem;
  color: black;
  letter-spacing: 0.4rem;
`;

const StyledBigTitle = styled.h1`
  font-size: 10.4rem;
  font-weight: 700;
  text-align: center;
  position: relative;
  color: black;
  line-height: 12rem;
  letter-spacing: -0.4rem;
`;

const StyledSmileImage = styled.img`
  position: absolute; 
  bottom: 0;
  left: -6.8rem;
  top: 7.6rem;
  width: 10rem;
`;

const StyledLineImage = styled.img`
  position: absolute; 
  right: 4.4rem;
  bottom: -4.4rem;
  width: 26rem;
`;

const StyledGlitterImage = styled.img`
  position: absolute; 
  bottom: 0;
  right: -3.2rem;
  top: 5.2rem;
  width: 6rem;
`;

const StlyedSectionGuideContainer = styled.div`
  position: absolute; 
  right: 14rem; 
  bottom: 6rem;
`;

const StlyedBlobContainer = styled.div`
  position: relative;
`;

const StyledGuideText = styled.span`
  position: absolute; 
  top: 7.2rem; 
  left: 4.2rem; 
  font-size: 2.4rem; 
  color: black;
`;

// section2
const StyledIntroduceSection = styled.section`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  scroll-snap-align: start;
  position: relative;
  justify-content: space-evenly;
`;

const StyledMainIntroduceText = styled.h1`
  font-size: 4rem;
  text-align: left;
  color: black;
  margin-bottom: 2rem;
  position: relative;
  font-weight: 700;
  line-height: 4.4rem;
  letter-spacing: 0.4rem;
`;

const StyledHighlightImage = styled.img`
  position: absolute; 
  left: -1.2rem; 
  top: 3.2rem;
`;

const StyledDetailIntroduceText = styled.span`
  font-size: 2rem;
  text-align: left;
  color: black;
  letter-spacing: 0.1rem; // TODO: 4단위
  line-height: 2.4rem;
`;

const StyledImageWrapper = styled.div`
  position: relative;
`;

// section3
const StyledServiceIntroduceSection = styled.section`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  scroll-snap-align: start;
  position: relative;
  flex-direction: column;
`;

const StyledExplanationTextContainer = styled.div`
  margin-bottom: 8rem;
`;

const StyledExplanationBigText = styled.h1(({ theme }) => `
  font-size: 4rem;
  text-align: left;
  color: ${theme.colors.BLACK_100};
  margin-bottom: 1.2rem;
  position: relative;
  font-weight: 700;
  line-height: 4.4rem;
  letter-spacing: 0.4rem
`);

const StyledExplanationSmallText = styled.div(({ theme }) => `
  font-size: 2rem;
  text-align: left;
  color: ${theme.colors.BLACK_100};
  letter-spacing: 0.1rem; // TODO: 4단위
  line-height: 2.4rem;
`);

const StyledServiceContainer = styled.div`
  display: flex; 
  gap: 4rem;
`;

const StyledServiceCircle = styled.div(({ theme }) => `
  position: relative;
  border-radius: 100%;
  width: 26rem;
  height: 26rem;
  background: ${theme.colors.YELLOW_100};
  margin-bottom: 1.2rem;
  display: flex;
  justify-content: center;
`);

const StyledServieHighlightImage = styled.img`
  position: absolute; 
  top: 1.2rem; 
  left: -4rem;
`;

const StyledServiceName = styled.div`
  text-align: center; 
  font-size: 2rem;
`;

const StyledServieGlitterImage = styled.img`
  position: absolute; 
  bottom: -2rem; 
  right: -1.2rem; 
  width: 4rem;
`;

const StyledPollImage = styled.img`
  width: 12rem;
`;

const StyledAppointmentImage = styled.img`
  width: 12rem;
`;

const StyledUndefinedImage = styled.img`
  width: 12rem;
`;

const StyledLink = styled.a`
  position: absolute;
  bottom: 10rem;
  text-decoration: none;
`;

const StyledGithubLogo = styled.img`
  width: 3.8rem;
`;

const StyledLoginText = styled.p(({ theme }) => `
  color: ${theme.colors.WHITE_100};
  padding: 2rem 0;
  font-size: 2rem;
`);

const StyledLoginContainer = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  gap: 2.4rem;
  width: 33.6rem;
  color: ${theme.colors.WHITE_100};
  background-color: ${theme.colors.BLACK_100};
  border-radius: 15px;
  padding: 1.2rem;
`
);

export default LandingPage;
