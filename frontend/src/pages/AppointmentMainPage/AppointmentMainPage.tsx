import styled from '@emotion/styled';

import AppointmentMainContainer from './components/AppointmentMainContainer/AppointmentMainContainer';
import AppointmentMainHeader from './components/AppointmentMainHeader/AppointmentMainHeader';

function AppointmentMainPage() {
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
