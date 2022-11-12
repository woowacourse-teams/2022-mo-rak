import { StyledStatus } from '@/pages/PollResultPage/components/PollResultStatus/PollResultStatus.styles';

import TextField from '@/components/TextField/TextField';

import { Poll } from '@/types/poll';
import { useTheme } from '@emotion/react';

type Props = {
  status: Poll['status'];
};

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
