import styled from '@emotion/styled';
import React from 'react';
import { useOutletContext } from 'react-router-dom';
import AppointmentMainContainer from '../../components/AppointmentMain/AppointmentMainContainer/AppointmentMainContainer';
import AppointmentMainHeader from '../../components/AppointmentMain/AppointmentMainHeader/AppointmentMainHeader';

function AppointmentMainPage() {
  const { setClickedMenu }: any = useOutletContext();
  setClickedMenu('appointment');

  return (
    <StyledContainer>
      <AppointmentMainHeader />
      <AppointmentMainContainer />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  flex-direction: column;
  gap: 4rem;
  padding: 6.4rem 20rem;
`;

export default AppointmentMainPage;
