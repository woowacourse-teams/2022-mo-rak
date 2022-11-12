import { MouseEventHandler, memo } from 'react';

import Button from '@/components/Button/Button';
import FlexContainer from '@/components/FlexContainer/FlexContainer';

import { useTheme } from '@emotion/react';

type Props = {
  onCancel: MouseEventHandler<HTMLButtonElement>;
};

function AppointmentCreateFormButtons({ onCancel }: Props) {
  const theme = useTheme();

  return (
    <FlexContainer justifyContent="space-between" gap="2rem">
      <Button
        variant="filled"
        colorScheme={theme.colors.GRAY_400}
        width="31.6rem"
        fontSize="4rem"
        onClick={onCancel}
      >
        취소
      </Button>
      <Button
        variant="filled"
        colorScheme={theme.colors.PURPLE_100}
        width="31.6rem"
        fontSize="4rem"
        type="submit"
      >
        생성
      </Button>
    </FlexContainer>
  );
}

export default memo(AppointmentCreateFormButtons, () => true);
