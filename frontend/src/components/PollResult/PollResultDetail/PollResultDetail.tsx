import { useTheme } from '@emotion/react';
import React from 'react';

import styled from '@emotion/styled';
import TextField from '../../@common/TextField/TextField';

import { PollInterface } from '../../../types/poll';

interface Props extends Pick<PollInterface, 'isAnonymous' | 'allowedPollCount' | 'closedAt'> {}

// TODO: 변수명 통일
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

function PollResultDetail({ isAnonymous, allowedPollCount, closedAt }: Props) {
  const theme = useTheme();

  return (
    <StyledContainer>
      <StyledCloseTime>
        {getFormattedClosedTime(closedAt)}
        까지
      </StyledCloseTime>
      <TextField
        width="6.4rem"
        borderRadius="20px"
        padding="1.2rem 0"
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
      >
        <StyledDetail>{isAnonymous ? '익명' : '기명'}</StyledDetail>
      </TextField>
      <TextField
        width="9.2rem"
        borderRadius="20px"
        padding="1.2rem 0"
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
      >
        <StyledDetail>
          {allowedPollCount === 1 ? '하나만 투표가능' : '여러개 투표가능'}
        </StyledDetail>
      </TextField>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  gap: 1.2rem;
  position: relative;
`;

const StyledCloseTime = styled.p`
  font-size: 1.2rem;
  position: absolute;
  right: 0;
`;

const StyledDetail = styled.span(
  ({ theme }) => `
  color: ${theme.colors.PURPLE_100};
  font-size: 0.8rem;
`
);

export default PollResultDetail;
