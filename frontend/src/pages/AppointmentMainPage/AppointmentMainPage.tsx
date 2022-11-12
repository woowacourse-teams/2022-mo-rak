import { StyledContainer } from '@/pages/AppointmentMainPage/AppointmentMainPage.styles';
import AppointmentMainContainer from '@/pages/AppointmentMainPage/components/AppointmentMainContainer/AppointmentMainContainer';
import AppointmentMainHeader from '@/pages/AppointmentMainPage/components/AppointmentMainHeader/AppointmentMainHeader';

function AppointmentMainPage() {
  return (
    <StyledContainer>
      <AppointmentMainHeader />
      <AppointmentMainContainer />
    </StyledContainer>
  );
}

export default AppointmentMainPage;
