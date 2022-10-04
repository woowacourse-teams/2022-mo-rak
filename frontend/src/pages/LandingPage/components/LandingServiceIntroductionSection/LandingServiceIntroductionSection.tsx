import { HTMLAttributes } from 'react';
import {
  StyledHighlightImage,
  StyledServiceIntroductionSection,
  StyledServicesImage,
  StyledSubTitle,
  StyledTitle
} from './LandingServiceIntroductionSection.styles';

import Services from '../../../../assets/services.svg';
import CircleHighlight from '../../../../assets/circle-highlight.svg';
import LandingNavbar from '../LandingNavbar/LandingNavbar';

interface Props extends HTMLAttributes<HTMLElement> {}

function LandingServiceIntroductionSection({ id }: Props) {
  return (
    <StyledServiceIntroductionSection id={id}>
      <LandingNavbar />
      <div>
        <StyledTitle>
          분산된 서비스...
          <br />
          불편하지 않으셨나요?
          <StyledHighlightImage src={CircleHighlight} alt="highlight" />
        </StyledTitle>
        <StyledSubTitle>
          투표 하기, 일정 정하기... 분산 되어있는 서비스에 불편함을 느끼지 않으셨나요?
          <br />
          모락에 모두 모여있어요!
        </StyledSubTitle>
      </div>
      <StyledServicesImage src={Services} alt="services" />
    </StyledServiceIntroductionSection>
  );
}

export default LandingServiceIntroductionSection;
