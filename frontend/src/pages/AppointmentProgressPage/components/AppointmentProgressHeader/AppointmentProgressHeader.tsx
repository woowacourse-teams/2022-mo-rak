import styled from '@emotion/styled';

import { AppointmentInterface } from '../../../../types/appointment';
import AppointmentProgressDetail from '../AppointmentProgressDetail/AppointmentProgressDetail';

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

const StyledHeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-height: 20.8rem;
`;

const StyledHeader = styled.header`
  font-size: 4.8rem;
`;

const StyledDescription = styled.div`
  font-size: 2.4rem;
  overflow-y: auto;
`;

export default AppointmentProgressHeader;
