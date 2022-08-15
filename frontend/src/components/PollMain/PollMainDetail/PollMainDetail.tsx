import { useTheme } from '@emotion/react';
import React from 'react';
import styled from '@emotion/styled';

import { PollInterface } from '../../../types/poll';
import TextField from '../../@common/TextField/TextField';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';

interface Props extends Pick<PollInterface, 'isAnonymous' | 'allowedPollCount' | 'closedAt'> {}

const getFormattedClosedTime = (value: string) => {
  const date = new Date(value);

  return date.toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    hour12: true
  });
};

function PollMainDetail({ isAnonymous, allowedPollCount, closedAt }: Props) {
  const theme = useTheme();

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      <StyledCloseTime>
        {getFormattedClosedTime(closedAt)}
        까지
      </StyledCloseTime>
      <FlexContainer gap="1.2rem">
        <TextField
          width="3.6rem"
          borderRadius="20px"
          variant="outlined"
          padding="0.4rem 0"
          colorScheme={theme.colors.PURPLE_100}
        >
          <StyledDetail>{isAnonymous ? '익명' : '기명'}</StyledDetail>
        </TextField>
        <TextField
          width="7.6rem"
          borderRadius="20px"
          variant="outlined"
          padding="0.4rem 0"
          colorScheme={theme.colors.PURPLE_100}
        >
          <StyledDetail>
            {allowedPollCount === 1 ? '하나만 투표가능' : '여러개 투표가능'}
          </StyledDetail>
        </TextField>
      </FlexContainer>
    </FlexContainer>
  );
}

const StyledCloseTime = styled.p`
  font-size: 0.8rem;
  align-self: end;
`;

const StyledDetail = styled.span(
  ({ theme }) => `
  color: ${theme.colors.PURPLE_100};
  font-size: 0.8rem;
`
);

export default PollMainDetail;
