import { HTMLAttributes } from 'react';

import {
  StyledAnchor,
  StyledBlobContainer,
  StyledBlobImage,
  StyledContainer,
  StyledGithubLogo,
  StyledGlitterImage,
  StyledLineImage,
  StyledLink,
  StyledLoginContainer,
  StyledLoginText,
  StyledLogo,
  StyledNextSectionGuideContainer,
  StyledSmileImage,
  StyledSubTitle,
  StyledTitle
} from '@/pages/LandingPage/components/LandingMainSection/LandingMainSection.styles';
import LandingNavbar from '@/pages/LandingPage/components/LandingNavbar/LandingNavbar';

import FlexContainer from '@/components//FlexContainer/FlexContainer';

import blobImg from '@/assets/blob.svg';
import githubLogoImg from '@/assets/github-logo.svg';
import glitterImg from '@/assets/glitter.svg';
import lineImg from '@/assets/line.svg';
import serviceLogoImg from '@/assets/service-logo.svg';
import smileImg from '@/assets/smile.svg';

type Props = HTMLAttributes<HTMLDivElement>;

function LandingMainSection({ id }: Props) {
  return (
    <StyledContainer id={id}>
      <LandingNavbar />
      <StyledLogo src={serviceLogoImg} alt="logo" />

      <FlexContainer flexDirection="column" gap="2rem">
        <StyledSubTitle>모락이 해줄게요</StyledSubTitle>
        <StyledTitle>
          <StyledSmileImage src={smileImg} alt="smile" />
          <StyledLineImage src={lineImg} alt="line" />
          <StyledGlitterImage src={glitterImg} alt="glitter" />
          모임을
          <br />
          즐겁게, 편하게!
        </StyledTitle>
        <StyledLink
          href={`https://github.com/login/oauth/authorize?client_id=${process.env.GITHUB_CLIENT_ID}`}
        >
          <StyledLoginContainer>
            <StyledGithubLogo src={githubLogoImg} alt="github-logo" />
            <StyledLoginText>GITHUB으로 로그인</StyledLoginText>
          </StyledLoginContainer>
        </StyledLink>
      </FlexContainer>
      <StyledNextSectionGuideContainer>
        <StyledBlobContainer>
          <StyledBlobImage src={blobImg} alt="blob" />
          <StyledAnchor href="#service-introduction-section">모락 소개</StyledAnchor>
        </StyledBlobContainer>
      </StyledNextSectionGuideContainer>
    </StyledContainer>
  );
}

export default LandingMainSection;
