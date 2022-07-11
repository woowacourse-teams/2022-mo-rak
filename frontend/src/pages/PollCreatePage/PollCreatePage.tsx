import styled from '@emotion/styled';
import React from 'react';
import PollCreateForm from '../../components/PollCreateForm/PollCreateForm';

function PollCreatePage() {
  return (
    <Container>
      <PollCreateForm />
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

export default PollCreatePage;
