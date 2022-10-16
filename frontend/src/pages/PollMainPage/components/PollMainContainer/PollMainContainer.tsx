import { useState, useEffect } from 'react';
import {
  StyledContainer,
  StyledGuide,
  StyledTitle,
  StyledLottieWrapper
} from './PollMainContainer.styles';
import { useParams } from 'react-router-dom';
import { useLottie } from 'lottie-react';
import Box from '../../../../components/Box/Box';
import PollMainStatus from '../PollMainStatus/PollMainStatus';
import PollMainDetail from '../PollMainDetail/PollMainDetail';
import PollMainProgress from '../PollMainProgress/PollMainProgress';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import MarginContainer from '../../../../components/MarginContainer/MarginContainer';

import { getPolls } from '../../../../api/poll';
import { getPollsResponse } from '../../../../types/poll';
import { Group } from '../../../../types/group';

import PollMainButtonGroup from '../PollMainButtonGroup/PollMainButtonGroup';
import emptyAnimation from '../../../../assets/empty-animation.json';

function PollMainContainer() {
  const [polls, setPolls] = useState<getPollsResponse>([]);
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const emptyLottie = useLottie({ animationData: emptyAnimation }, { width: '60rem' });

  useEffect(() => {
    (async () => {
      const res = await getPolls(groupCode);
      setPolls(res.data);
    })();
  }, []);

  if (polls.length <= 0) {
    return (
      <>
        {/* TODO: 재사용 가능하지 않을까한다! */}
        <StyledLottieWrapper>{emptyLottie.View}</StyledLottieWrapper>
        <StyledGuide aria-label="poll-guide">첫 투표를 만들어보세요!</StyledGuide>
      </>
    );
  }

  return (
    <StyledContainer>
      {polls.map(({ status, title, code, isAnonymous, allowedPollCount, closedAt, count }) => (
        <Box
          key={code}
          width="36.4rem"
          padding="2.8rem"
          filter={status === 'CLOSED' ? 'grayscale(1)' : 'none'}
        >
          <FlexContainer justifyContent="end">
            <PollMainStatus status={status} />
          </FlexContainer>
          <StyledTitle>{title}</StyledTitle>
          <PollMainProgress currentParticipants={count} groupCode={groupCode} />
          <MarginContainer margin="0 0 1.6rem">
            {/* TODO: 'detail' 컴포넌트명 변경 고민(전체 페이지 수정 필요) */}
            <PollMainDetail
              isAnonymous={isAnonymous}
              allowedPollCount={allowedPollCount}
              closedAt={closedAt}
            />
          </MarginContainer>
          <PollMainButtonGroup pollCode={code} status={status} />
        </Box>
      ))}
    </StyledContainer>
  );
}

export default PollMainContainer;
