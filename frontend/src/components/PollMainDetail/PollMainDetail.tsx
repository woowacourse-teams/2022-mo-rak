import { useTheme } from '@emotion/react';
import React from 'react';

import FlexContainer from '../common/FlexContainer/FlexContainer';

import { PollInterface } from '../../types/poll';
import TextField from '../common/TextField/TextField';

interface Props extends Pick<PollInterface, 'isAnonymous' | 'allowedPollCount'> {}

function PollMainDetail({
  isAnonymous,
  allowedPollCount
}: Props) {
  const theme = useTheme();

  return (
    <FlexContainer gap="1.2rem">
      <TextField
        width="3.6rem"
        borderRadius="20px"
        variant="outlined"
        fontSize="0.8rem"
        padding="0.4rem 0"
        colorScheme={theme.colors.PURPLE_100}
      >
        {isAnonymous ? '익명' : '기명'}
      </TextField>
      <TextField
        width="7.6rem"
        borderRadius="20px"
        variant="outlined"
        fontSize="0.8rem"
        padding="0.4rem 0"
        colorScheme={theme.colors.PURPLE_100}

      >
        {allowedPollCount === 1 ? '하나만 투표가능' : '여러개 투표가능'}
      </TextField>
    </FlexContainer>
  );
}

export default PollMainDetail;
