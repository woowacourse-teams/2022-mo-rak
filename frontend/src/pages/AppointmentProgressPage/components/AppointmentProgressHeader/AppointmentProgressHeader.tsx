import { Appointment } from '@/types/appointment';
import AppointmentProgressDetail from '@/pages/AppointmentProgressPage/components/AppointmentProgressDetail/AppointmentProgressDetail';
import {
  StyledHeaderContainer,
  StyledHeader,
  StyledDescription
} from '@/pages/AppointmentProgressPage/components/AppointmentProgressHeader/AppointmentProgressHeader.styles';

type Props = {
  appointment: Appointment;
};

function AppointmentProgressHeader({ appointment }: Props) {
  return (
    <StyledHeaderContainer>
      <StyledHeader>{appointment.title}</StyledHeader>
      <StyledDescription>{appointment.description}</StyledDescription>
      <AppointmentProgressDetail
        durationHours={appointment.durationHours}
        durationMinutes={appointment.durationMinutes}
        startTime={appointment.startTime}
        endTime={appointment.endTime}
      />
    </StyledHeaderContainer>
  );
}

export default AppointmentProgressHeader;
