import AppointmentCreateHeader from '@/pages/AppointmentCreatePage/components/AppointmentCreateHeader/AppointmentCreateHeader';
import AppointmentCreateInputs from '@/pages/AppointmentCreatePage/components/AppointmentCreateInputs/AppointmentCreateInputs';
import {
  StyledContainer,
  StyledContentContainer
} from '@/pages/AppointmentCreatePage/AppointmentCreatePage.styles';

function AppointmentCreatePage() {
  return (
    <StyledContainer>
      <StyledContentContainer>
        <AppointmentCreateHeader />
        <AppointmentCreateInputs />
      </StyledContentContainer>
    </StyledContainer>
  );
}

export default AppointmentCreatePage;
