import { AppointmentInterface } from '../../../../types/appointment';
import AppointmentProgressDetail from '../AppointmentProgressDetail/AppointmentProgressDetail';
import {
  StyledHeaderContainer,
  StyledHeader,
  StyledDescription
} from './AppointmentProgressHeader.style';

interface Props {
  appointment: AppointmentInterface;
}

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
