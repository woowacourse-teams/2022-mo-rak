import { useTheme } from '@emotion/react';
import { StyledStatus } from './PollMainStatus.styles';
import TextField from '../../../../components/TextField/TextField';
import { Poll } from '../../../../types/poll';

type Props = {
  status: Poll['status'];
};

function PollMainStatus({ status }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="filled"
      width="20%"
      padding="0.8rem 0"
      borderRadius="5px"
      colorScheme={status === 'OPEN' ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
    >
      <StyledStatus>{status === 'OPEN' ? '진행중' : '완료'}</StyledStatus>
    </TextField>
  );
}

export default PollMainStatus;
