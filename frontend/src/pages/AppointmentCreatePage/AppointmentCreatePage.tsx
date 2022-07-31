import styled from '@emotion/styled';
import React from 'react';
import Box from '../../components/common/Box/Box';
import AppointmentCreateForm from '../../components/AppointmentCreate/AppointmentCreateForm/AppointmentCreateForm';

function AppointmentCreatePage() {
  return (
    <StyledContainer>
      <StyledLeftContainer>
        <StyledHeader>약속을 생성하세요</StyledHeader>
        <StyledHeaderContent>여러분이 만날날을 생성해주세요~</StyledHeaderContent>
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

const StyledHeader = styled.header`
  font-size: 4rem;
`;

const StyledHeaderContent = styled.p`
  font-size: 2rem;
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
