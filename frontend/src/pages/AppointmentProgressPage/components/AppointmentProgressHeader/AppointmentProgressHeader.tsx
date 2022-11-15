import { Appointment } from '@/types/appointment';
import AppointmentProgressHeaderDetail from '@/pages/AppointmentProgressPage/components/AppointmentProgressHeaderDetail/AppointmentProgressHeaderDetail';
import {
  StyledContainer,
  StyledHeader,
  StyledDescription
} from '@/pages/AppointmentProgressPage/components/AppointmentProgressHeader/AppointmentProgressHeader.styles';

type Props = {
  appointment: Appointment;
};

function AppointmentProgressHeader({ appointment }: Props) {
  return (
    <StyledContainer>
      <StyledHeader>{appointment.title}</StyledHeader>
      <StyledDescription>{appointment.description}</StyledDescription>
      <AppointmentProgressHeaderDetail
        durationHours={appointment.durationHours}
        durationMinutes={appointment.durationMinutes}
        startTime={appointment.startTime}
        endTime={appointment.endTime}
      />
    </StyledContainer>
  );
}

export default AppointmentProgressHeader;
