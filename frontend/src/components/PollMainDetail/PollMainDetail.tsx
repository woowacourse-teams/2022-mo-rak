import { useTheme } from '@emotion/react';
import React from 'react';

import Button from '../common/Button/Button';
import FlexContainer from '../common/FlexContainer/FlexContainer';

import { PollInterface } from '../../types/poll';

interface Props extends Pick<PollInterface, 'isAnonymous' | 'allowedPollCount'> {}

// TODO: 의미상 button이 아닌데, button disabled로 만드는 것이 맞을까?
function PollMainDetail({
  isAnonymous,
  allowedPollCount
}: Props) {
  const theme = useTheme();

  return (
    <FlexContainer gap="1.2rem">
      <Button
        width="3.6rem"
        borderRadius="20px"
        variant="outlined"
        fontSize="0.8rem"
        padding="0.4rem 0"
        colorScheme={theme.colors.PURPLE_100}
        disabled
      >
        {isAnonymous ? '익명' : '기명'}
      </Button>
      <Button
        width="7.6rem"
        borderRadius="20px"
        variant="outlined"
        fontSize="0.8rem"
        padding="0.4rem 0"
        colorScheme={theme.colors.PURPLE_100}
        disabled
      >
        {allowedPollCount === 1 ? '하나만 투표가능' : '여러개 투표가능'}
      </Button>
    </FlexContainer>
  );
}

export default PollMainDetail;
