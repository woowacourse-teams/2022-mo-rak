import { useTheme } from '@emotion/react';

import { StyledCloseTime, StyledDetail } from './PollMainDetail.styles';

import { Poll } from '../../../../types/poll';
import TextField from '../../../../components/TextField/TextField';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

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

type Props = Pick<Poll, 'isAnonymous' | 'allowedPollCount' | 'closedAt'>;

function PollMainDetail({ isAnonymous, allowedPollCount, closedAt }: Props) {
  const theme = useTheme();

  return (
    <FlexContainer flexDirection="column" gap="1.6rem">
      <StyledCloseTime>
        {getFormattedClosedTime(closedAt)}
        까지
      </StyledCloseTime>
      <FlexContainer gap="1.6rem">
        <TextField
          padding="0.8rem 1.6rem"
          borderRadius="20px"
          variant="outlined"
          colorScheme={theme.colors.PURPLE_100}
        >
          <StyledDetail>{isAnonymous ? '익명' : '기명'}</StyledDetail>
        </TextField>
        <TextField
          padding="0.8rem"
          borderRadius="20px"
          variant="outlined"
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

export default PollMainDetail;
