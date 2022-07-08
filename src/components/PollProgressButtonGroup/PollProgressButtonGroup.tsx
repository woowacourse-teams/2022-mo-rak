import { useTheme } from '@emotion/react';
import React from 'react';

import Button from '../common/Button/Button';
import FlexContainer from '../common/FlexContainer/FlexContainer';

import { PollInterface } from '../../types/poll';

function PollProgressButtonGroup({
  isAnonymous,
  allowedPollCount
}: Pick<PollInterface, 'isAnonymous' | 'allowedPollCount'>) {
  const theme = useTheme();

  return (
    <FlexContainer gap="1.2rem">
      <Button
        width="6.4rem"
        height="3.6rem"
        borderRadius="20px"
        variant="outlined"
        fontSize="0.8rem"
        colorScheme={theme.colors.PURPLE_100}
        disabled
      >
        {isAnonymous ? '익명' : '기명'}
      </Button>
      <Button
        width="9.2rem"
        height="3.6rem"
        borderRadius="20px"
        variant="outlined"
        fontSize="0.8rem"
        colorScheme={theme.colors.PURPLE_100}
        disabled
      >
        {allowedPollCount === 1 ? '하나만 투표가능' : '여러개 투표가능'}
      </Button>
    </FlexContainer>
  );
}

export default PollProgressButtonGroup;
