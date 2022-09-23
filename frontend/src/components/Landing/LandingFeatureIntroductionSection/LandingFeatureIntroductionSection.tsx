import { HTMLAttributes } from 'react';
import styled from '@emotion/styled';

import Glitter from '../../../assets/glitter.svg';
import Highlight from '../../../assets/highlight.svg';
import Poll from '../../../assets/poll-small.svg';
import Appointment from '../../../assets/time.svg';
import Undefined from '../../../assets/question.svg';
import LandingNavbar from '../LandingNavbar/LandingNavbar';

interface Props extends HTMLAttributes<HTMLElement> {}

function LandingFeatureIntroductionSection({ id }: Props) {
  return (
    <StyledFeatureIntroductionSection id={id}>
      <LandingNavbar />
      
      <StyledIntroductionTitleContainer>
        <StyledTitle>모락을 통해서 할 수 있어요!</StyledTitle>
        <StyledSubTitle>
          모락이 모임을 더 편하고, 즐겁게 할 수 있도록 도와줄게요
        </StyledSubTitle>
      </StyledIntroductionTitleContainer>

      <StyledFeatureContainer>
        <div>
          <StyledFeatureCircle>
            <StyledPollImage src={Poll} alt="poll" />
            <StyledHighlightImage src={Highlight} alt="highlight" />
          </StyledFeatureCircle>
          <StyledFeatureName>투표하기</StyledFeatureName>
        </div>

        <div>
          <StyledFeatureCircle>
            <StyledAppointmentImage src={Appointment} alt="appointment" />
          </StyledFeatureCircle>
          <StyledFeatureName>약속잡기</StyledFeatureName>
        </div>

        <div>
          <StyledFeatureCircle>
            <StyledUndefinedImage src={Undefined} alt="undefined" />
            <StyledGlitterImage src={Glitter} alt="glitter" />
          </StyledFeatureCircle>
          <StyledFeatureName>coming soon!</StyledFeatureName>
        </div>

        <div>
          <StyledFeatureCircle>
            <StyledUndefinedImage src={Undefined} alt="undefined" />
          </StyledFeatureCircle>
          <StyledFeatureName>coming soon!</StyledFeatureName>
        </div>
      </StyledFeatureContainer>
    </StyledFeatureIntroductionSection>
  );
}

const StyledFeatureIntroductionSection = styled.section`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  height: 100vh;
  scroll-snap-align: start;
`;

const StyledIntroductionTitleContainer = styled.div`
  margin-bottom: 8rem;
`;

const StyledTitle = styled.h1(
  ({ theme }) => `
  font-size: 4rem;
  text-align: left;
  color: ${theme.colors.BLACK_100};
  margin-bottom: 1.2rem;
  position: relative;
  font-weight: 700;
  line-height: 4.4rem;
  letter-spacing: 0.4rem
`
);

const StyledSubTitle = styled.div(
  ({ theme }) => `
  font-size: 2rem;
  text-align: left;
  color: ${theme.colors.BLACK_100};
  letter-spacing: 0.1rem; 
  line-height: 2.4rem;
`
);

const StyledFeatureContainer = styled.div`
  display: flex;
  gap: 4rem;
`;

const StyledFeatureCircle = styled.div(
  ({ theme }) => `
  position: relative;
  border-radius: 100%;
  width: 26rem;
  height: 26rem;
  background: ${theme.colors.YELLOW_100};
  margin-bottom: 1.2rem;
  display: flex;
  justify-content: center;
`
);

const StyledHighlightImage = styled.img`
  position: absolute;
  top: 1.2rem;
  left: -4rem;
  width: 5.2rem;
  height: 5.6rem;
`;

const StyledFeatureName = styled.div`
  text-align: center;
  font-size: 2rem;
`;

const StyledGlitterImage = styled.img`
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

export default LandingFeatureIntroductionSection;
