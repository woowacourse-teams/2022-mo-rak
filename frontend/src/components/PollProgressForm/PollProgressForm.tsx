import React, { FormEvent, useContext, useEffect, useState } from 'react';

import { useNavigate } from 'react-router-dom';
import Box from '../common/Box/Box';
import Divider from '../common/Divider/Divider';
import MarginContainer from '../common/MarginContainer/MarginContainer';
import PollTitle from '../PollTitle/PollTitle';

import PollProgressSubmitButton from '../PollProgressSubmitButton/PollProgressSubmitButton';
import { PollContextStore } from '../../contexts/PollContext';
import { getPollInfo, progressPoll } from '../../api/poll';
import PollProgressRadioGroup from '../PollProgressRadioGroup/PollProgressRadioGroup';
import PollProgressButtonGroup from '../PollProgressButtonGroup/PollProgressButtonGroup';
import { PollInterface, PollItemInterface } from '../../types/poll';

function PollProgressForm() {
  const navigate = useNavigate();
  const pollContext = useContext(PollContextStore);
  const [pollInfo, setPollInfo] = useState<PollInterface>();
  const [selectedPollItem, setSelectedPollItem] = useState<PollItemInterface>();

  // TODO: 객체로 state로 관리하는 것에 단점이 분명히 있다. 리팩토링 필요
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      if (selectedPollItem && pollInfo) {
        console.log('끝');
        await progressPoll(pollInfo.id, { itemIds: [selectedPollItem.id] });
        navigate('/result');
      }
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    const fetchPollInfo = async (pollId: string) => {
      const res = await getPollInfo(pollId);
      setPollInfo(res);
    };

    try {
      const pollId = pollContext?.pollId;

      if (pollId) {
        fetchPollInfo(pollId);
      }

      // TODO: pollid가 없을 때 메인 화면으로 보내주기!
    } catch (err) {
      alert(err);
    }
  }, []);

  return (
    // TODO: 화면 작았다 켜지는 것 수정
    <Box width="84.4rem" minHeight="65.2rem" padding="6.4rem 4.8rem">
      {pollInfo ? (
        <form onSubmit={handleSubmit}>
          <MarginContainer margin="0 0 4rem 0">
            <PollTitle title={pollInfo.title} />
            <Divider />
          </MarginContainer>
          <MarginContainer margin="0 0 1.6rem 0">
            <PollProgressButtonGroup
              isAnonymous={pollInfo.isAnonymous}
              allowedPollCount={pollInfo.allowedPollCount}
            />
          </MarginContainer>
          <MarginContainer margin="0 0 4rem 0">
            <PollProgressRadioGroup
              pollId={pollInfo.id}
              selectedPollItem={selectedPollItem}
              setSelectedPollItem={setSelectedPollItem}
            />
          </MarginContainer>
          <PollProgressSubmitButton />
        </form>
      ) : (
        <div>로딩중</div>
      )}
    </Box>
  );
}

export default PollProgressForm;
