import styled from '@emotion/styled';
import React from 'react';
import { useParams } from 'react-router-dom';
import PollProgressForm from '../../components/PollProgressForm/PollProgressForm';

function PollProgressPage() {
  const { id } = useParams();

  return (
    <StyledContainer>
      <PollProgressForm pollId={Number(id)} />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default PollProgressPage;
