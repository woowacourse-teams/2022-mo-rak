import { useTheme } from '@emotion/react';

import { StyledDetail } from './PollProgressDetail.styles';
import TextField from '@/components/TextField/TextField';
import FlexContainer from '@/components/FlexContainer/FlexContainer';

import { Poll } from '@/types/poll';

type Props = Pick<Poll, 'isAnonymous' | 'allowedPollCount'>;

function PollProgressDetail({ isAnonymous, allowedPollCount }: Props) {
  const theme = useTheme();

  return (
    <FlexContainer gap="1.2rem">
      <TextField
        borderRadius="20px"
        padding="1.2rem 2rem"
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
      >
        <StyledDetail>{isAnonymous ? '익명' : '기명'}</StyledDetail>
      </TextField>
      <TextField
        borderRadius="20px"
        padding="1.2rem"
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
      >
        <StyledDetail>
          {allowedPollCount === 1 ? '하나만 투표가능' : '여러개 투표가능'}
        </StyledDetail>
      </TextField>
    </FlexContainer>
  );
}

export default PollProgressDetail;
