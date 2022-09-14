import { HTMLAttributes } from 'react';
import styled from '@emotion/styled';

import Logo from '../../../assets/logo.svg';
import Smile from '../../../assets/smile.svg';
import Line from '../../../assets/line.svg';
import Blob from '../../../assets/blob.svg';
import GithubLogo from '../../../assets/github-logo.svg';
import Circle from '../../../assets/half-circle.svg';
import Glitter from '../../../assets/glitter.svg';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';

interface Props extends HTMLAttributes<HTMLElement> {}

function LandingMainSection({ id }: Props) {
  return (
    <StyledMainSection id={id}>
      <StyledNavbar>
        <StyledMenu href="#main-section">LOGIN</StyledMenu>
        <StyledMenu href="#introduce-section">ABOUT</StyledMenu>
        <StyledMenu href="#service-introduce-section">FEATURES</StyledMenu>
      </StyledNavbar>
      <StyledLogo src={Logo} alt="logo" />

      <FlexContainer flexDirection="column" gap="2rem">
        <StyledSmallTitle>모락이 해줄게요</StyledSmallTitle>
        <StyledBigTitle>
          <StyledSmileImage src={Smile} alt="smile" />
          <StyledLineImage src={Line} alt="line" />
          <StyledGlitterImage src={Glitter} alt="glitter" />
          모임을
          <br />
          즐겁게, 편하게!
        </StyledBigTitle>
        <StyledLink
          href={`https://github.com/login/oauth/authorize?client_id=${process.env.GITHUB_CLIENT_ID}`}
        >
          <StyledLoginContainer>
            <StyledGithubLogo src={GithubLogo} alt="github-logo" />
            <StyledLoginText>GITHUB으로 로그인</StyledLoginText>
          </StyledLoginContainer>
        </StyledLink>
      </FlexContainer>
      <StyledSectionGuideContainer>
        <StlyedBlobContainer>
          <StyledBlobImage src={Blob} alt="blob"/>
          <StyledGuideText>모락 소개</StyledGuideText>
        </StlyedBlobContainer>
      </StyledSectionGuideContainer>
    </StyledMainSection>
  );
}

const StyledLink = styled.a`
  text-decoration: none;
  margin-top: 8rem;
`;

const StyledGithubLogo = styled.img`
  width: 3.8rem;
`;

const StyledLoginText = styled.p(
  ({ theme }) => `
  color: ${theme.colors.WHITE_100};
  padding: 2rem 0;
  font-size: 2rem;
`
);

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
  margin: 0 auto;
`
);

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

const StyledLogo = styled.img`
  width: 10rem;
  position: absolute;
  top: 2rem;
  left: 4rem;
`;

const StyledMenu = styled.a(
  ({ theme }) => `
  text-decoration: none;
  color: ${theme.colors.BLACK_100};
`
);

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
  background-position: bottom 0 right 0;
`;

const StyledSmallTitle = styled.div(
  ({ theme }) => `
  font-size: 2.4rem;
  margin-bottom: 1.2rem;
  color: ${theme.colors.BLACK_100};
  letter-spacing: 0.4rem;
  margin: 0 auto;
  padding-top: 20rem;
`
);

const StyledBigTitle = styled.h1(
  ({ theme }) => `
  font-size: 10.4rem;
  font-weight: 700;
  text-align: center;
  position: relative;
  color: ${theme.colors.BLACK_100};
  line-height: 12rem;
  letter-spacing: -0.4rem;
`
);

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

const StyledSectionGuideContainer = styled.div`
  position: absolute;
  right: 14rem;
  bottom: 6rem;
`;

const StlyedBlobContainer = styled.div`
  position: relative;
`;

const StyledBlobImage = styled.img`
  width: 18rem;
  height: 17.4rem;
`;

const StyledGuideText = styled.span(
  ({ theme }) => `
  position: absolute; 
  top: 7.2rem; 
  left: 4.2rem; 
  font-size: 2.4rem; 
  color: ${theme.colors.BLACK_100};
`
);

export default LandingMainSection;
