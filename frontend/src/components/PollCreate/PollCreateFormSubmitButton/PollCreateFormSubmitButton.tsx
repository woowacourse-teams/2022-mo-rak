import { useTheme } from '@emotion/react';
import { memo } from 'react';

import Button from '../../@common/Button/Button';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';

function PollCreateFormSubmitButton() {
  const theme = useTheme();

  return (
    <FlexContainer justifyContent="center">
      <Button
        variant="filled"
        width="100%"
        padding="2rem 0"
        colorScheme={theme.colors.PURPLE_100}
        fontSize="2rem"
        type="submit"
      >
        투표 만들기
      </Button>
    </FlexContainer>
  );
}

export default memo(PollCreateFormSubmitButton);
