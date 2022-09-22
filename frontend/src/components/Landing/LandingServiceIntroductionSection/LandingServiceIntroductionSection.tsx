import { HTMLAttributes } from 'react';
import styled from '@emotion/styled';

import Services from '../../../assets/services.svg';
import CircleHighlight from '../../../assets/circle-highlight.svg';
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


const StyledServiceIntroductionSection = styled.section`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  scroll-snap-align: start;
  position: relative;
  justify-content: space-evenly;
`;

const StyledTitle = styled.h1(
  ({ theme }) => `
  position: relative;
  text-align: left;
  font-size: 4rem;
  margin-bottom: 2rem;
  font-weight: 700;
  line-height: 4.4rem;
  letter-spacing: 0.4rem;
  color: ${theme.colors.BLACK_100};
`
);

const StyledHighlightImage = styled.img`
  position: absolute;
  left: -1.2rem;
  top: 3.2rem;
  width: 10.4rem;
  height: 6.8rem;
`;

const StyledSubTitle = styled.span(
  ({ theme }) => `
  font-size: 2rem;
  text-align: left;
  letter-spacing: 0.1rem; 
  line-height: 2.4rem;
  color: ${theme.colors.BLACK_100};
`
);

const StyledServicesImage = styled.img`
  width: 67.6rem;
  height: 63.6rem;
`;

export default LandingServiceIntroductionSection;
