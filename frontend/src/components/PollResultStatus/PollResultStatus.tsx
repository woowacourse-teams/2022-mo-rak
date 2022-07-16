import React from 'react';
import { useTheme } from '@emotion/react';
import Button from '../common/Button/Button';
import { PollInterface } from '../../types/poll';

interface Props {
  status: PollInterface['status']
}

function PollResultStatus({ status }: Props) {
  const theme = useTheme();

  return (
    <Button
      variant="filled"
      width="6.4rem"
      padding="1.6rem 0"
      fontSize="1.2rem"
      color={theme.colors.WHITE_100}
      colorScheme={theme.colors.PURPLE_100}
      disabled
    >
      {status === 'OPEN' ? '진행중' : '완료'}
    </Button>
  );
}

export default PollResultStatus;
