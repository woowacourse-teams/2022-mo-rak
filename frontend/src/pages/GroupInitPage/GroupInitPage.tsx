import React from 'react';
import styled from '@emotion/styled';
import Logo from '../../assets/logo.svg';

import GroupCreateForm from '../../components/GroupCreateForm/GroupCreateForm';
import GroupParticipateForm from '../../components/GroupParticipateForm/GroupParticipateForm';
import Box from '../../components/common/Box/Box';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';

function GroupInitPage() {
  return (
    <Container>
      <StyledLogo src={Logo} alt="logo" />
      <Box width="60rem" minHeight="51.6rem" padding="8.4rem 3.2rem">
        <FlexContainer flexDirection="column" gap="6.8rem">
          <GroupCreateForm />
          <GroupParticipateForm />
        </FlexContainer>
      </Box>
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

const StyledLogo = styled.img`
  width: 35.6rem;
`;

export default GroupInitPage;
