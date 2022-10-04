import { AppointmentInterface } from '../../../../types/appointment';
import { StyledDuration } from './AppointmentProgressDetail.style';

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
      <br />({startTime}~{endTime})
    </StyledDuration>
  );
}

export default AppointmentProgressDetail;
