import { HTMLAttributes } from 'react';
import {
  StyledHighlightImage,
  StyledContainer,
  StyledServicesImage,
  StyledSubTitle,
  StyledTitle,
  StyledTitleContainer
} from '@/pages/LandingPage/components/LandingServiceIntroductionSection/LandingServiceIntroductionSection.styles';

import servicesImg from '@/assets/services.svg';
import circleHighlightImg from '@/assets/circle-highlight.svg';
import LandingNavbar from '@/pages/LandingPage/components/LandingNavbar/LandingNavbar';

type Props = HTMLAttributes<HTMLDivElement>;

function LandingServiceIntroductionSection({ id }: Props) {
  return (
    <StyledContainer id={id}>
      <LandingNavbar />
      <StyledTitleContainer>
        <StyledTitle>
          분산된 서비스...
          <br />
          불편하지 않으셨나요?
          <StyledHighlightImage src={circleHighlightImg} alt="highlight" />
        </StyledTitle>
        <StyledSubTitle>
          투표 하기, 약속 잡기, 역할 정하기...
          <br />
          분산 되어있는 서비스에 불편함을 느끼지 않으셨나요?
          <br />
          모락에 모두 모여있어요!
        </StyledSubTitle>
      </StyledTitleContainer>
      <StyledServicesImage src={servicesImg} alt="services" />
    </StyledContainer>
  );
}

export default LandingServiceIntroductionSection;
