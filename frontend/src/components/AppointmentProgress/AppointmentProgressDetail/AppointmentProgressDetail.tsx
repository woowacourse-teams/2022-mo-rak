import styled from '@emotion/styled';

import { AppointmentInterface } from '../../../types/appointment';

interface Props {
  durationHours: AppointmentInterface['durationHours'];
  durationMinutes: AppointmentInterface['durationMinutes'];
  startTime: AppointmentInterface['startTime'];
  endTime: AppointmentInterface['endTime'];
}

function AppointmentProgressDetail({ durationHours, durationMinutes, startTime, endTime }: Props) {
  return (
      <StyledDuration>
        약속시간은 {durationHours}시간 {durationMinutes}분동안 진행 
        <br/>({startTime}~{endTime})
      </StyledDuration>
  );
}

const StyledDuration = styled.p`
  font-size: 2.4rem;
`;

export default AppointmentProgressDetail;
