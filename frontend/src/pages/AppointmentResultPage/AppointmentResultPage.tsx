import React from 'react';
import styled from '@emotion/styled';
import AppointmentResultContainer from '../../components/AppointmentResult/AppointmentResultContainer/AppointmentResultContainer';

function AppointmentResultPage() {
  return (
    <StyledContainer>
      {/* TODO: 피그마를 기반으로 컴포넌트들 보여주기 */}
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
