import AppointmentProgressDetail from '@/pages/AppointmentProgressPage/components/AppointmentProgressDetail/AppointmentProgressDetail';
import {
  StyledContainer,
  StyledDescription,
  StyledHeader
} from '@/pages/AppointmentProgressPage/components/AppointmentProgressHeader/AppointmentProgressHeader.styles';

import { Appointment } from '@/types/appointment';

type Props = {
  appointment: Appointment;
};

function AppointmentProgressHeader({ appointment }: Props) {
  return (
    <StyledContainer>
      <StyledHeader>{appointment.title}</StyledHeader>
      <StyledDescription>{appointment.description}</StyledDescription>
      <AppointmentProgressDetail
        durationHours={appointment.durationHours}
        durationMinutes={appointment.durationMinutes}
        startTime={appointment.startTime}
        endTime={appointment.endTime}
      />
    </StyledContainer>
  );
}

export default AppointmentProgressHeader;
