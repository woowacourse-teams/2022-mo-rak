import { useLottie } from 'lottie-react';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import PollMainButtons from '@/pages/PollMainPage/components/PollMainButtons/PollMainButtons';
import {
  StyledContainer,
  StyledGuide,
  StyledLottieContainer,
  StyledPollContainer,
  StyledTitle
} from '@/pages/PollMainPage/components/PollMainContainer/PollMainContainer.styles';
import PollMainDetail from '@/pages/PollMainPage/components/PollMainDetail/PollMainDetail';
import PollMainProgress from '@/pages/PollMainPage/components/PollMainProgress/PollMainProgress';
import PollMainStatus from '@/pages/PollMainPage/components/PollMainStatus/PollMainStatus';

import Box from '@/components/Box/Box';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import MarginContainer from '@/components/MarginContainer/MarginContainer';

import { getPolls } from '@/api/poll';
import emptyLottie from '@/assets/empty-animation.json';
import { Group } from '@/types/group';
import { getPollsResponse } from '@/types/poll';

function PollMainContainer() {
  const [polls, setPolls] = useState<getPollsResponse>([]);
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const emptyAnimation = useLottie({ animationData: emptyLottie }, { width: '60rem' });

  useEffect(() => {
    (async () => {
      const res = await getPolls(groupCode);
      setPolls(res.data);
    })();
  }, []);

  if (polls.length <= 0) {
    return (
      <>
        <StyledLottieContainer>{emptyAnimation.View}</StyledLottieContainer>
        <StyledGuide aria-label="poll-guide">첫 투표를 만들어보세요!</StyledGuide>
      </>
    );
  }

  return (
    <StyledContainer>
      {polls.map(({ status, title, code, isAnonymous, allowedPollCount, closedAt, count }) => (
        // TODO: StyledPollContainer 를 삭제해주거나 Box와 합쳐주기
        <StyledPollContainer key={code}>
          <Box width="100%" padding="2.8rem" filter={status === 'CLOSED' ? 'grayscale(1)' : 'none'}>
            <FlexContainer justifyContent="end">
              <PollMainStatus status={status} />
            </FlexContainer>
            <StyledTitle>{title}</StyledTitle>
            <PollMainProgress currentParticipants={count} />
            <MarginContainer margin="0 0 1.6rem">
              {/* TODO: 'detail' 컴포넌트명 변경 고민(전체 페이지 수정 필요) */}
              <PollMainDetail
                isAnonymous={isAnonymous}
                allowedPollCount={allowedPollCount}
                closedAt={closedAt}
              />
            </MarginContainer>
            <PollMainButtons pollCode={code} status={status} />
          </Box>
        </StyledPollContainer>
      ))}
    </StyledContainer>
  );
}

export default PollMainContainer;
