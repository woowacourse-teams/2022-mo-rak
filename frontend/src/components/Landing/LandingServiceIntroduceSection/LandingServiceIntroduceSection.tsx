import React from 'react';
import styled from '@emotion/styled';

import Glitter from '../../../assets/glitter.svg';
import Highlight from '../../../assets/highlight.svg';
import Poll from '../../../assets/poll-small.svg';
import Appointment from '../../../assets/time.svg';
import Undefined from '../../../assets/question.svg';

function LandingServiceIntroduceSection() {
  return (
    <StyledServiceIntroduceSection>
      <StyledNavbar>
        <li>LOGIN</li>
        <li>ABOUT</li>
        <li>CONTACT US</li>
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
  );
}

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

export default LandingServiceIntroduceSection;
