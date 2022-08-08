import { useTheme } from '@emotion/react';
import React from 'react';
import Button from '../../common/Button/Button';

function GroupParticipateFormSubmitButton() {
  const theme = useTheme();

  return (
    <Button variant="filled" colorScheme={theme.colors.PURPLE_100} fontSize="1.6rem" type="submit">
      참가하기
    </Button>
  );
}

export default GroupParticipateFormSubmitButton;
