import React from 'react';
import styled from '@emotion/styled';

import GroupCreateForm from '../../components/GroupCreateForm/GroupCreateForm';
import GroupParticipateForm from '../../components/GroupParticipateForm/GroupParticipateForm';

function GroupInitPage() {
  return (
    <Container>
      <GroupCreateForm />
      <GroupParticipateForm />
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 6.4rem;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
`;

export default GroupInitPage;
