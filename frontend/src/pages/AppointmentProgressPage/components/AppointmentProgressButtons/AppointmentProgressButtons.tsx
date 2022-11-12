import { MouseEventHandler } from 'react';

import Button from '@/components/Button/Button';

import { useTheme } from '@emotion/react';

type Props = {
  onClickProgress: MouseEventHandler<HTMLButtonElement>;
};

function AppointmentProgressButtons({ onClickProgress }: Props) {
  const theme = useTheme();

  return (
    <Button
      variant="filled"
      colorScheme={theme.colors.PURPLE_100}
      width="100%"
      fontSize="4rem"
      onClick={onClickProgress}
    >
      선택
    </Button>
  );
}

export default AppointmentProgressButtons;
