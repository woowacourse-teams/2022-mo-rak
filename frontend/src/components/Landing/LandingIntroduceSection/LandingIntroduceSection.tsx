import React from 'react';
import styled from '@emotion/styled';

import Services from '../../../assets/service-group.svg';
import CircleHighlight from '../../../assets/circle-highlight.svg';

function LandingIntroduceSection() {
  return (
    <StyledIntroduceSection>
      <StyledNavbar>
        <li>LOGIN</li>
        <li>ABOUT</li>
        <li>CONTACT US</li>
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

const StyledIntroduceSection = styled.section`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  scroll-snap-align: start;
  position: relative;
  justify-content: space-evenly;
`;

const StyledMainIntroduceText = styled.h1(({ theme }) => `
  font-size: 4rem;
  text-align: left;
  color: ${theme.colors.BLACK_100};
  margin-bottom: 2rem;
  position: relative;
  font-weight: 700;
  line-height: 4.4rem;
  letter-spacing: 0.4rem;
`);

const StyledHighlightImage = styled.img`
  position: absolute; 
  left: -1.2rem; 
  top: 3.2rem;
`;

const StyledDetailIntroduceText = styled.span(({ theme }) => `
  font-size: 2rem;
  text-align: left;
  color: ${theme.colors.BLACK_100};
  letter-spacing: 0.1rem; // TODO: 4단위
  line-height: 2.4rem;
`);

const StyledImageWrapper = styled.div`
  position: relative;
`;

export default LandingIntroduceSection;
