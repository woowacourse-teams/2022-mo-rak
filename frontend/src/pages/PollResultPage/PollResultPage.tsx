import styled from '@emotion/styled';
import React from 'react';
import { useParams } from 'react-router-dom';

import PollResultContainer from '../../components/PollResultContainer/PollResultContainer';

function PollResultPage() {
  const { id } = useParams();

  return (
    <StyledContainer>
      <PollResultContainer pollId={Number(id)} />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default PollResultPage;
