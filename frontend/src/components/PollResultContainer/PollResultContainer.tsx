import React, { useEffect, useState } from 'react';

import Box from '../common/Box/Box';
import Divider from '../common/Divider/Divider';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import MarginContainer from '../common/MarginContainer/MarginContainer';

import PollTitle from '../PollTitle/PollTitle';
import PollResultItemGroup from '../PollResultItemGroup/PollResultItemGroup';
import PollResultDetail from '../PollResultDetail/PollResultDetail';
import PollResultButtonGroup from '../PollResultButtonGroup/PollResultButtonGroup';
import { getPoll, getPollResult } from '../../api/poll';
import { PollInterface, PollItemResultType } from '../../types/poll';
import PollResultProgress from '../PollResultProgress/PollResultProgress';
import PollResultStatus from '../PollResultStatus/PollResultStatus';
import PollResultShareLink from '../PollResultShareLink/PollResultShareLink';

interface Props {
  pollId: PollInterface['id'];
}
function PollResultContainer({ pollId }: Props) {
  const [poll, setPoll] = useState<PollInterface>();
  const [pollResult, setPollResult] = useState<Array<PollItemResultType>>([]);

  useEffect(() => {
    const fetchPoll = async (pollId: PollInterface['id']) => {
      const res = await getPoll(pollId);
      setPoll(res);
    };

    const fetchPollResult = async (pollId: PollInterface['id']) => {
      const res = await getPollResult(pollId);
      setPollResult(res);
    };

    try {
      fetchPoll(pollId);
      fetchPollResult(pollId);

      // TODO: pollid가 없을 때 메인 화면으로 보내주기!
    } catch (err) {
      alert(err);
    }
  }, []);

  return (
    <Box width="84.4rem" padding="2rem 4.8rem 5.6rem">
      {poll && pollResult ? (
        <>
          <FlexContainer justifyContent="end">
            <MarginContainer margin="0 0 1.4rem 0">
              <FlexContainer gap="1.2rem" alignItems="center">
                <PollResultShareLink pollId={pollId} />
                <PollResultStatus status={poll.status} />
              </FlexContainer>
            </MarginContainer>
          </FlexContainer>
          <MarginContainer margin="0 0 1.4rem 0">
            <PollTitle title={poll.title} />
            <Divider />
          </MarginContainer>
          <MarginContainer margin="1.4rem 0">
            <PollResultProgress pollResult={pollResult} />
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
              <PollResultItemGroup status={poll.status} pollId={poll.id} />
            </FlexContainer>
          </MarginContainer>
          <PollResultButtonGroup status={poll.status} pollId={poll.id} />
        </>
      ) : (
        <div>로딩중</div>
      )}
    </Box>
  );
}

export default PollResultContainer;
