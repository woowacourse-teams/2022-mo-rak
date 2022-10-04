import AppointmentMainContainer from './components/AppointmentMainContainer/AppointmentMainContainer';
import AppointmentMainHeader from './components/AppointmentMainHeader/AppointmentMainHeader';
import { StyledContainer } from './AppointmentMainPage.style';

function AppointmentMainPage() {
  return (
    <StyledContainer>
      <AppointmentMainHeader />
      <AppointmentMainContainer />
    </StyledContainer>
  );
}

export default AppointmentMainPage;
