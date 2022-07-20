import React from 'react';
import { useTheme } from '@emotion/react';
import TextField from '../common/TextField/TextField';
import { PollInterface } from '../../types/poll';

interface Props {
  status: PollInterface['status']
}

function PollResultStatus({ status }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="filled"
      width="6.4rem"
      padding="1.6rem 0"
      fontSize="1.2rem"
      borderRadius="1.2rem"
      color={theme.colors.WHITE_100}
      colorScheme={status === 'OPEN' ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
    >
      {status === 'OPEN' ? '진행중' : '완료'}
    </TextField>
  );
}

export default PollResultStatus;
