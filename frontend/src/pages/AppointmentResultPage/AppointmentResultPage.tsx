import React from 'react';
import styled from '@emotion/styled';
import AppointmentResultContainer from '../../components/AppointmentResult/AppointmentResultContainer/AppointmentResultContainer';

function AppointmentResultPage() {
  return (
    <StyledContainer>
      <AppointmentResultContainer />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default AppointmentResultPage;
