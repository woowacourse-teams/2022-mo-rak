import { HTMLAttributes } from 'react';
import {
  StyledBlobContainer,
  StyledBlobImage,
  StyledGithubLogo,
  StyledGlitterImage,
  StyledLineImage,
  StyledLink,
  StyledLoginContainer,
  StyledLoginText,
  StyledLogo,
  StyledContainer,
  StyledNextSectionGuideContainer,
  StyledSmileImage,
  StyledSubTitle,
  StyledTitle,
  StyledAnchor
} from './LandingMainSection.styles';

import Logo from '../../../../assets/logo.svg';
import Smile from '../../../../assets/smile.svg';
import Line from '../../../../assets/line.svg';
import Blob from '../../../../assets/blob.svg';
import GithubLogo from '../../../../assets/github-logo.svg';
import Glitter from '../../../../assets/glitter.svg';
import FlexContainer from '../../../../components//FlexContainer/FlexContainer';
import LandingNavbar from '../LandingNavbar/LandingNavbar';

type Props = HTMLAttributes<HTMLDivElement>;

function LandingMainSection({ id }: Props) {
  return (
    <StyledContainer id={id}>
      <LandingNavbar />
      <StyledLogo src={Logo} alt="logo" />

      <FlexContainer flexDirection="column" gap="2rem">
        <StyledSubTitle>모락이 해줄게요</StyledSubTitle>
        <StyledTitle>
          <StyledSmileImage src={Smile} alt="smile" />
          <StyledLineImage src={Line} alt="line" />
          <StyledGlitterImage src={Glitter} alt="glitter" />
          모임을
          <br />
          즐겁게, 편하게!
        </StyledTitle>
        <StyledLink
          href={`https://github.com/login/oauth/authorize?client_id=${process.env.GITHUB_CLIENT_ID}`}
        >
          <StyledLoginContainer>
            <StyledGithubLogo src={GithubLogo} alt="github-logo" />
            <StyledLoginText>GITHUB으로 로그인</StyledLoginText>
          </StyledLoginContainer>
        </StyledLink>
      </FlexContainer>
      <StyledNextSectionGuideContainer>
        <StyledBlobContainer>
          <StyledBlobImage src={Blob} alt="blob" />
          <StyledAnchor href="#service-introduction-section">모락 소개</StyledAnchor>
        </StyledBlobContainer>
      </StyledNextSectionGuideContainer>
    </StyledContainer>
  );
}

export default LandingMainSection;
