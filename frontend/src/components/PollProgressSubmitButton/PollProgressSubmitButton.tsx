import { useTheme } from '@emotion/react';
import React from 'react';

import Button from '../common/Button/Button';
import FlexContainer from '../common/FlexContainer/FlexContainer';

function PollProgressSubmitButton() {
  const theme = useTheme();

  return (
    <FlexContainer justifyContent="center">
      <Button
        variant="filled"
        width="46rem"
        height="6.4rem"
        colorScheme={theme.colors.PURPLE_100}
        color={theme.colors.WHITE_100}
        fontSize="2rem"
        type="submit"
      >
        투표하기
      </Button>
    </FlexContainer>
  );
}

export default PollProgressSubmitButton;
