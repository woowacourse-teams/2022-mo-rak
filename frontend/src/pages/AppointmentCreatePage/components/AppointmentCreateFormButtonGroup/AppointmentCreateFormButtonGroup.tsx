import { memo, MouseEventHandler } from 'react';
import { useTheme } from '@emotion/react';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import Button from '../../../../components/Button/Button';

interface Props {
  onCancel: MouseEventHandler<HTMLButtonElement>;
}

function AppointmentCreateFormButtonGroup({ onCancel }: Props) {
  const theme = useTheme();

  return (
    <FlexContainer justifyContent="space-between">
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

export default memo(AppointmentCreateFormButtonGroup, () => true);
