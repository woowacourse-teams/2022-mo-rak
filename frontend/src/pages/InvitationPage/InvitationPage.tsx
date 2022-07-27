import styled from '@emotion/styled';
import React from 'react';
import InvitationContainer from '../../components/Invitation/InvitationContainer/InvitationContainer';

function InvitationPage() {
  return (
    <StyledContainer>
      <InvitationContainer />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 6.4rem;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
`;

export default InvitationPage;
