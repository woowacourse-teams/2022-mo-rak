import { useTheme } from '@emotion/react';
import { MouseEventHandler } from 'react';
import Button from '../../../../components/Button/Button';

interface Props {
  onClickProgress: MouseEventHandler<HTMLButtonElement>;
}

function AppointmentProgressButtonGroup({ onClickProgress }: Props) {
  const theme = useTheme();

  return (
    <Button
      variant="filled"
      colorScheme={theme.colors.PURPLE_100}
      width="30rem"
      fontSize="4rem"
      onClick={onClickProgress}
    >
      선택
    </Button>
  );
}

export default AppointmentProgressButtonGroup;
