import { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useParams } from 'react-router-dom';
import { useLottie } from 'lottie-react';
import Box from '../../@common/Box/Box';
import PollMainStatus from '../PollMainStatus/PollMainStatus';
import PollMainDetail from '../PollMainDetail/PollMainDetail';
import PollMainProgress from '../PollMainProgress/PollMainProgress';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import MarginContainer from '../../@common/MarginContainer/MarginContainer';

import { getPolls } from '../../../api/poll';
import { getPollsResponse } from '../../../types/poll';
import { GroupInterface } from '../../../types/group';

import PollMainButtonGroup from '../PollMainButtonGroup/PollMainButtonGroup';
import emptyAnimation from '../../../assets/empty-animation.json';

function PollMainContainer() {
  const [polls, setPolls] = useState<getPollsResponse>([]);
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const emptyLottie = useLottie({ animationData: emptyAnimation }, { width: '60rem' }); 

  useEffect(() => {
    const fetchPolls = async () => {
      const res = await getPolls(groupCode);
      setPolls(res.data);
    };

    try {
      fetchPolls();
    } catch (err) {
      alert(err);
    }
  }, []);

  if (polls.length <= 0) {
    return (
      <>
        {/* TODO: 재사용 가능하지 않을까한다! */}
        <LottieWrapper>{emptyLottie.View}</LottieWrapper>
        <StyledGuide>첫 투표를 만들어보세요!</StyledGuide>
      </>
    );
  }

  return (
    <StyledContainer>
      {polls.length > 0 &&
        polls.map(({ status, title, code, isAnonymous, allowedPollCount, closedAt, count }) => (
          <Box
            key={code}
            width="36.4rem"
            padding="2.8rem"
            minHeight="23.2rem"
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

const StyledContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 3.2rem;
`;

const StyledTitle = styled.h1`
  font-size: 2.2rem;
  text-align: center;
`;

const LottieWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const StyledGuide = styled.p(
  ({ theme }) => `
  text-align: center;
  font-size: 4rem;

  color: ${theme.colors.GRAY_400}
`
);

export default PollMainContainer;
