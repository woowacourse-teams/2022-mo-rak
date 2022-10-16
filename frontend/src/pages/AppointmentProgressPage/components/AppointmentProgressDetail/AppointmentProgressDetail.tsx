import { Appointment } from '../../../../types/appointment';
import { StyledDuration } from './AppointmentProgressDetail.styles';

type Props = {
  durationHours: Appointment['durationHours'];
  durationMinutes: Appointment['durationMinutes'];
  startTime: Appointment['startTime'];
  endTime: Appointment['endTime'];
};

function AppointmentProgressDetail({ durationHours, durationMinutes, startTime, endTime }: Props) {
  return (
    <StyledDuration>
      약속시간은 {durationHours}시간 {durationMinutes}분동안 진행
      <br />({startTime}~{endTime})
    </StyledDuration>
  );
}

export default AppointmentProgressDetail;
