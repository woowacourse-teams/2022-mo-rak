import React from 'react';
import { useTheme } from '@emotion/react';
import Button from '../common/Button/Button';
import { PollInterface } from '../../types/poll';

interface Props {
  status: PollInterface['status'];
}

function PollMainStatus({ status }: Props) {
  const theme = useTheme();

  return (
    <Button
      variant="filled"
      width="4rem"
      padding="0.4rem 0"
      borderRadius="5px"
      color={theme.colors.WHITE_100}
      colorScheme={theme.colors.PURPLE_100}
      disabled
    >
      {status === 'OPEN' ? '진행중' : '완료'}
    </Button>
  );
}

export default PollMainStatus;
