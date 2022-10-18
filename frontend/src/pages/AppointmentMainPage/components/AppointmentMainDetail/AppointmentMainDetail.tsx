import { useTheme } from '@emotion/react';

import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import TextField from '../../../../components/TextField/TextField';
import { Appointment } from '../../../../types/appointment';
import { getFormattedHourMinuteDuration } from '../../../../utils/date';
import { StyledCloseTime, StyledDetail } from './AppointmentMainDetail.styles';

const getFormattedClosedTime = (value: string) => {
  const date = new Date(value);

  return date.toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    hour12: true
  });
};

type Props = {
  durationHours: Appointment['durationHours'];
  durationMinutes: Appointment['durationMinutes'];
  closedAt: Appointment['closedAt'];
};

function AppointmentMainDetail({ durationHours, durationMinutes, closedAt }: Props) {
  console.log(durationHours, durationMinutes);
  const theme = useTheme();

  return (
    <FlexContainer flexDirection="column" gap="1.6rem">
      <StyledCloseTime>
        {getFormattedClosedTime(closedAt)}
        까지
      </StyledCloseTime>
      {/* TODO: flex 쓰지 않고 padding을 줄 수 있도록 만들어보기 */}
      <FlexContainer>
        <TextField
          padding="0.8rem 1.6rem"
          borderRadius="20px"
          variant="outlined"
          colorScheme={theme.colors.PURPLE_100}
        >
          <StyledDetail>
            {getFormattedHourMinuteDuration(durationHours, durationMinutes)}
          </StyledDetail>
        </TextField>
      </FlexContainer>
    </FlexContainer>
  );
}

export default AppointmentMainDetail;
