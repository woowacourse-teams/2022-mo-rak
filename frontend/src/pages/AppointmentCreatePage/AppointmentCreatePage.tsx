import styled from '@emotion/styled';
import React from 'react';
import Box from '../../components/common/Box/Box';
import AppointmentCreateForm from '../../components/AppointmentCreate/AppointmentCreateForm/AppointmentCreateForm';
import AppointmentCreateHeader from '../../components/AppointmentCreate/AppointmentCreateHeader/AppointmentCreateHeadert';

function AppointmentCreatePage() {
  return (
    <StyledContainer>
      <StyledLeftContainer>
        <AppointmentCreateHeader />
        <Box width="45.2rem" minHeight="60rem" />
      </StyledLeftContainer>
      <StyledRightContainer>
        <AppointmentCreateForm />
      </StyledRightContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  width: calc(100% - 36.4rem);
  align-items: center;
  justify-content: center;
  gap: 6rem;
`;

const StyledLeftContainer = styled.div`
  display: flex;
  width: 45.2rem;
  flex-direction: column;
  gap: 1.8rem;
`;

const StyledRightContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4rem;
`;

export default AppointmentCreatePage;
