import styled from '@emotion/styled';
import React from 'react';
import PollMainContainer from '../../components/PollMain/PollMainContainer/PollMainContainer';

// TODO: 다른 페이지와 똑같이 추상화
function PollMainPage() {
  console.log('폴 메인 페이지');
  // TODO: 완료, 진행중에 따라 다르게 렌더링
  return (
    <StyledContainer>
      <PollMainContainer />
    </StyledContainer>
  );
}

// TODO: width 수정, 계속해서 여기에 들어가야하는가? app에서 감싸주면 안될까?
const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  padding: 6.4rem 20rem;
`;

export default PollMainPage;
