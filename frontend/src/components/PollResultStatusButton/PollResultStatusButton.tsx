import React from 'react';
import { useTheme } from '@emotion/react';
import Button from '../common/Button/Button';

interface Props {
  status: string;
}

function PollResultStatusButton({ status }: Props) {
  const theme = useTheme();

  return (
    <Button
      variant="filled"
      width="6.4rem"
      height="4.4rem"
      color={theme.colors.WHITE_100}
      colorScheme={theme.colors.PURPLE_100}
      disabled
    >
      {status === 'OPEN' ? '진행중' : '완료'}
    </Button>
  );
}

export default PollResultStatusButton;
