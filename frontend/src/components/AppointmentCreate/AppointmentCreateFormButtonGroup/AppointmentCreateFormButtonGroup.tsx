import React from 'react';
import { useTheme } from '@emotion/react';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Button from '../../@common/Button/Button';

function AppointmentCreateFormButtonGroup() {
  const theme = useTheme();

  return (
    <FlexContainer justifyContent="center" gap="0.8rem">
      <Button variant="filled" colorScheme={theme.colors.GRAY_400} width="31.6rem" fontSize="4rem">
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

export default AppointmentCreateFormButtonGroup;
