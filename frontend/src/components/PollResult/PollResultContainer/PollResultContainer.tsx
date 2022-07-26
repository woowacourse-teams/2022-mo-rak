import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';

import { useParams } from 'react-router-dom';
import Box from '../../common/Box/Box';
import Divider from '../../common/Divider/Divider';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import MarginContainer from '../../common/MarginContainer/MarginContainer';

import PollResultItemGroup from '../PollResultItemGroup/PollResultItemGroup';
import PollResultDetail from '../PollResultDetail/PollResultDetail';
import PollResultButtonGroup from '../PollResultButtonGroup/PollResultButtonGroup';
import { getPoll, getPollResult } from '../../../api/poll';
import { PollInterface, PollItemResultType } from '../../../types/poll';
import PollResultProgress from '../PollResultProgress/PollResultProgress';
import PollResultStatus from '../PollResultStatus/PollResultStatus';
import PollResultShareLink from '../PollResultShareLink/PollResultShareLink';

function PollResultContainer() {
  const { groupCode, pollId } = useParams();
  const [poll, setPoll] = useState<PollInterface>();
  const [pollResult, setPollResult] = useState<Array<PollItemResultType>>([]);

  useEffect(() => {
    const fetchPoll = async (pollId: PollInterface['id']) => {
      if (groupCode) {
        const res = await getPoll(pollId, groupCode);
        setPoll(res);
      }
    };

    const fetchPollResult = async (pollId: PollInterface['id']) => {
      if (groupCode) {
        const res = await getPollResult(pollId, groupCode);
        setPollResult(res);
      }
    };

    try {
      if (pollId) {
        // TODO: useParams를 받을 때, pollId를 number로 애초에 바꿀 수는 없을까?
        fetchPoll(Number(pollId));
        fetchPollResult(Number(pollId));
      }

      // TODO: pollid가 없을 때 메인 화면으로 보내주기!
    } catch (err) {
      alert(err);
    }
  }, []);

  return (
    <Box width="84.4rem" padding="2rem 4.8rem 5.6rem">
      {/* TODO: typescript에 의한 undefined 에러를 해결하기 위한 임시 방편, 안전해서 좋지만, 좀 더 깔끔하게 코드를 작성할 수는 없을까? */}
      {poll && pollResult && pollId && groupCode ? (
        <>
          <FlexContainer justifyContent="end">
            <MarginContainer margin="0 0 1.4rem 0">
              <FlexContainer gap="1.2rem" alignItems="center">
                <PollResultShareLink pollId={Number(pollId)} />
                <PollResultStatus status={poll.status} />
              </FlexContainer>
            </MarginContainer>
          </FlexContainer>
          <MarginContainer margin="0 0 1.4rem 0">
            <StyledTitle>{poll.title}</StyledTitle>
            <Divider />
          </MarginContainer>
          <MarginContainer margin="1.4rem 0">
            <PollResultProgress pollResult={pollResult} groupCode={groupCode} />
          </MarginContainer>
          <MarginContainer margin="1.4rem 0">
            {/* TODO: PollInterface의 allowedPollCount type string 지우기(임시) */}
            <PollResultDetail
              isAnonymous={poll.isAnonymous}
              allowedPollCount={poll.allowedPollCount}
            />
          </MarginContainer>
          <MarginContainer margin="0 0 4rem 0">
            <FlexContainer flexDirection="column" gap="1.2rem">
              <PollResultItemGroup
                status={poll.status}
                pollId={poll.id}
                groupCode={groupCode}
                pollResult={pollResult}
              />
            </FlexContainer>
          </MarginContainer>
          <PollResultButtonGroup
            isHost={poll.isHost}
            status={poll.status}
            pollId={poll.id}
            groupCode={groupCode}
          />
        </>
      ) : (
        <div>로딩중</div>
      )}
    </Box>
  );
}

const StyledTitle = styled.h1(
  ({ theme }) => `
  color: ${theme.colors.BLACK_100};
  font-size: 3.2rem;
`
);

export default PollResultContainer;
