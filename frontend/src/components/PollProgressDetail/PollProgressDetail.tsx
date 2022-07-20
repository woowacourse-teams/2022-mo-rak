import { useTheme } from '@emotion/react';
import React from 'react';

import TextField from '../common/TextField/TextField';
import FlexContainer from '../common/FlexContainer/FlexContainer';

import { PollInterface } from '../../types/poll';

interface Props extends Pick<PollInterface, 'isAnonymous' | 'allowedPollCount'> {}

function PollProgressDetail({
  isAnonymous,
  allowedPollCount
}: Props) {
  const theme = useTheme();

  return (
    <FlexContainer gap="1.2rem">
      <TextField
        width="6.4rem"
        borderRadius="20px"
        padding="1.2rem 0"
        variant="outlined"
        fontSize="0.8rem"
        colorScheme={theme.colors.PURPLE_100}
        color={theme.colors.PURPLE_100}
      >
        {isAnonymous ? '익명' : '기명'}
      </TextField>
      <TextField
        width="9.2rem"
        borderRadius="20px"
        padding="1.2rem 0"
        variant="outlined"
        fontSize="0.8rem"
        colorScheme={theme.colors.PURPLE_100}
        color={theme.colors.PURPLE_100}
      >
        {allowedPollCount === 1 ? '하나만 투표가능' : '여러개 투표가능'}
      </TextField>
    </FlexContainer>
  );
}

export default PollProgressDetail;
