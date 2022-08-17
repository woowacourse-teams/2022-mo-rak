import styled from '@emotion/styled';
import React from 'react';
import AppointmentMainContainer from '../../components/AppointmentMain/AppointmentMainContainer/AppointmentMainContainer';
import AppointmentMainHeader from '../../components/AppointmentMain/AppointmentMainHeader/AppointmentMainHeader';
import { useMenuDispatch } from '../../context/MenuProvider';

function AppointmentMainPage() {
  const dispatch = useMenuDispatch();
  dispatch({ type: 'SET_CLICKED_MENU', payload: 'appointment' });

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
