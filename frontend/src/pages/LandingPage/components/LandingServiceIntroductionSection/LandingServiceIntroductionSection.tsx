import { HTMLAttributes } from 'react';
import {
  StyledHighlightImage,
  StyledContainer,
  StyledServicesImage,
  StyledSubTitle,
  StyledTitle,
  StyledTitleContainer
} from './LandingServiceIntroductionSection.styles';

import Services from '../../../../assets/services.svg';
import CircleHighlight from '../../../../assets/circle-highlight.svg';
import LandingNavbar from '../LandingNavbar/LandingNavbar';

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
          <StyledHighlightImage src={CircleHighlight} alt="highlight" />
        </StyledTitle>
        <StyledSubTitle>
          투표 하기, 일정 정하기...
          <br />
          분산 되어있는 서비스에 불편함을 느끼지 않으셨나요?
          <br />
          모락에 모두 모여있어요!
        </StyledSubTitle>
      </StyledTitleContainer>
      <StyledServicesImage src={Services} alt="services" />
    </StyledContainer>
  );
}

export default LandingServiceIntroductionSection;
