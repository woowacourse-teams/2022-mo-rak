import styled from '@emotion/styled';
import React from 'react';
import { Link } from 'react-router-dom';

import NotFound from '../../assets/blinking-eye.gif';

function NotFoundPage() {
  return (
    <StyledBackground>
      <StyledNotFoundImage src={NotFound} alt="not-found-image" />
      <StyledGuideMessage>
        <StyledSorryMessage>앗 죄송합니다, 문제가 생겼어요!</StyledSorryMessage>
        <div>
          걱정하지마세요.&nbsp;
          <StyledLink to="/">메인페이지</StyledLink>
          로 돌아갈 수 있어요 :)
        </div>
      </StyledGuideMessage>
    </StyledBackground>
  );
}

const StyledBackground = styled.div`
  height: 100vh;
  background: black;
`;

const StyledNotFoundImage = styled.img`
  transform: translate(-50%, -50%);
  position: absolute;
  top: 50%;
  left: 50%;
`;

const StyledSorryMessage = styled.div`
  font-size: 6rem;
`;

const StyledGuideMessage = styled.div(({ theme }) => `
  position: absolute;
  left: 0;
  right: 0;
  bottom: 20rem;
  color: ${theme.colors.WHITE_100};
  font-size: 4rem;
  text-align: center;
  line-height: 7.2rem;
`);

const StyledLink = styled(Link)(({ theme }) => `
  color: ${theme.colors.WHITE_100};
`);

export default NotFoundPage;
