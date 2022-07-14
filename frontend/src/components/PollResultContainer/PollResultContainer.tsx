import React, { useEffect, useContext, useState } from 'react';

import Box from '../common/Box/Box';
import Divider from '../common/Divider/Divider';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import MarginContainer from '../common/MarginContainer/MarginContainer';

import PollTitle from '../PollTitle/PollTitle';
import PollProgressButtonGroup from '../PollProgressButtonGroup/PollProgressButtonGroup';
import PollResultItemGroup from '../PollResultItemGroup/PollResultItemGroup';
import PollResultButtonGroup from '../PollResultButtonGroup/PollResultButtonGroup';
import { PollContextStore } from '../../contexts/PollContext';
import { getPollInfo, getPollResult } from '../../api/poll';
import { PollInterface, PollResultInterface } from '../../types/poll';
import PollResultProgress from '../PollResultProgress/PollResultProgrss';
import PollResultStatusButton from '../PollResultStatusButton/PollResultStatusButton';

function PollResultContainer() {
  const [pollInfo, setPollInfo] = useState<PollInterface>();
  const [pollResult, setPollResult] = useState<Array<PollResultInterface>>();
  const pollContext = useContext(PollContextStore);

  useEffect(() => {
    const fetchPollInfo = async (pollId: PollInterface['id']) => {
      const res = await getPollInfo(pollId);
      setPollInfo(res);
    };

    const fetchPollResult = async (pollId: PollInterface['id']) => {
      const res = await getPollResult(pollId);
      setPollResult(res);
    };

    try {
      const pollId = pollContext?.pollId;

      if (pollId) {
        fetchPollInfo(pollId);
        fetchPollResult(pollId);
      }

      // TODO: pollid가 없을 때 메인 화면으로 보내주기!
    } catch (err) {
      alert(err);
    }
  }, []);

  return (
    <Box width="84.4rem" minHeight="65.2rem" padding="1.8rem 4.8rem 6.4rem 4.8rem">
      {pollInfo && pollResult ? (
        <>
          <FlexContainer justifyContent="end">
            <MarginContainer margin="0 0 1.4rem 0">
              <PollResultStatusButton status={pollInfo.status} />
            </MarginContainer>
          </FlexContainer>
          <MarginContainer margin="0 0 1.5rem 0">
            <PollTitle title={pollInfo.title} />
            <Divider />
          </MarginContainer>
          <MarginContainer margin="1.4rem 0 1.5rem 0">
            <PollResultProgress pollResult={pollResult} />
          </MarginContainer>
          <MarginContainer margin="1.4rem 0 1.5rem 0">
            {/* TODO: PollProgressButtonGroup과 같음 (여긴 결과 페이지) -> 컴포넌트 분리 */}
            {/* TODO: PollInterface의 allowedPollCount type string 지우기(임시) */}
            <PollProgressButtonGroup
              isAnonymous={pollInfo.isAnonymous}
              allowedPollCount={pollInfo.allowedPollCount}
            />
          </MarginContainer>
          <MarginContainer margin="0 0 4rem 0">
            <FlexContainer flexDirection="column" gap="1.2rem">
              <PollResultItemGroup pollId={pollInfo.id} />
            </FlexContainer>
          </MarginContainer>
          <PollResultButtonGroup pollId={pollInfo.id} />
        </>
      ) : (
        <div>로딩중</div>
      )}
    </Box>
  );
}

export default PollResultContainer;
