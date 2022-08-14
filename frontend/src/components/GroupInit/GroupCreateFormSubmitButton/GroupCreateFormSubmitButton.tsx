import React from 'react';
import { useTheme } from '@emotion/react';
import Button from '../../@common/Button/Button';

function GroupCreateFormSubmitButton() {
  const theme = useTheme();

  return (
    <Button type="submit" variant="filled" colorScheme={theme.colors.PURPLE_100} fontSize="1.6rem">
      생성하기
    </Button>
  );
}

export default GroupCreateFormSubmitButton;
