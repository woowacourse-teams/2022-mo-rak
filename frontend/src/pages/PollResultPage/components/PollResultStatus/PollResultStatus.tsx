import { useTheme } from '@emotion/react';
import TextField from '../../../../components/TextField/TextField';
import { PollInterface } from '../../../../types/poll';
import { StyledStatus } from './PollResultStatus.styles';

interface Props {
  status: PollInterface['status'];
}

function PollResultStatus({ status }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="filled"
      width="8rem"
      padding="1.6rem 0"
      borderRadius="1.2rem"
      colorScheme={status === 'OPEN' ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
    >
      <StyledStatus aria-label="poll-status">{status === 'OPEN' ? '진행중' : '완료'}</StyledStatus>
    </TextField>
  );
}

export default PollResultStatus;
