import { useTheme } from '@emotion/react';
import TextField from '../../../../components/TextField/TextField';
import { Appointment } from '../../../../types/appointment';
import { StyledStatus } from './AppointmentResultStatus.styles';

type Props = {
  isClosed: Appointment['isClosed'];
};

function AppointmentResultStatus({ isClosed }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="filled"
      width="12.8rem"
      padding="2.6rem 0"
      borderRadius="1.2rem"
      colorScheme={isClosed ? theme.colors.GRAY_400 : theme.colors.PURPLE_100}
    >
      <StyledStatus>{isClosed ? '완료' : '진행중'}</StyledStatus>
    </TextField>
  );
}

export default AppointmentResultStatus;
