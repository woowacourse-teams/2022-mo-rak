import styled from '@emotion/styled';
import React from 'react';

import PollResultContainer from '../../components/PollResultContainer/PollResultContainer';

function PollResultPage() {
  return (
    <Container>
      <PollResultContainer />
    </Container>
  );
}

const Container = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 21.2rem 35.2rem;
`;

export default PollResultPage;
