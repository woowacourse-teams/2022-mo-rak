import styled from '@emotion/styled';
import React from 'react';
import PollCreateForm from '../../components/PollCreate/PollCreateForm/PollCreateForm';

function PollCreatePage() {
  return (
    <StyledContainer>
      <PollCreateForm />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default PollCreatePage;
