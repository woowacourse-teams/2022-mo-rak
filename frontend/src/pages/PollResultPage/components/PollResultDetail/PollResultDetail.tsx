import { useTheme } from '@emotion/react';

import { StyledContainer, StyledCloseTime, StyledDetail } from './PolLResultDetail.styles';
import TextField from '../../../../components/TextField/TextField';

import { PollInterface } from '../../../../types/poll';

interface Props extends Pick<PollInterface, 'isAnonymous' | 'allowedPollCount' | 'closedAt'> {}

// TODO: 변수명 통일해주자! formatted?
// TODO: 메인페이지에서도 사용하는 데 어디에 이 함수를 놓을 수 있을지 고민해보자!
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
    </StyledContainer>
  );
}

export default PollResultDetail;
