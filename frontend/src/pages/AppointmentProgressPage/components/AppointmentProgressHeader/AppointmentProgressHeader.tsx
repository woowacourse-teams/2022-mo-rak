import { Appointment } from '../../../../types/appointment';
import AppointmentProgressDetail from '../AppointmentProgressDetail/AppointmentProgressDetail';
import {
  StyledContainer,
  StyledHeader,
  StyledDescription
} from './AppointmentProgressHeader.styles';

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
