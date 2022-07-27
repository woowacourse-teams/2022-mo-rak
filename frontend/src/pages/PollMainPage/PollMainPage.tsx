import styled from '@emotion/styled';
import React from 'react';
import PollMainContainer from '../../components/PollMain/PollMainContainer/PollMainContainer';
import PollMainHeader from '../../components/PollMain/PollMainHeader/PollMainHeader';

function PollMainPage() {
  // TODO: 완료, 진행중에 따라 다르게 렌더링
  return (
    <StyledContainer>
      <PollMainHeader />
      {/* TODO: 네이밍 */}
      <PollMainContainer />
    </StyledContainer>
  );
}

// TODO: width 수정, 계속해서 여기에 들어가야하는가? app에서 감싸주면 안될까?
const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4rem;
  width: calc(100% - 36.4rem);
  padding: 6.4rem 20rem;
`;

export default PollMainPage;
