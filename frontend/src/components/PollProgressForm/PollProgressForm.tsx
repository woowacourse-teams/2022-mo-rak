import React, { ChangeEvent, FormEvent, useEffect, useState } from 'react';

import { useNavigate, useParams } from 'react-router-dom';
import Box from '../common/Box/Box';
import Divider from '../common/Divider/Divider';
import MarginContainer from '../common/MarginContainer/MarginContainer';
import PollTitle from '../PollTitle/PollTitle';

import PollProgressSubmitButton from '../PollProgressSubmitButton/PollProgressSubmitButton';
import { getPoll, progressPoll } from '../../api/poll';
import PollProgressItemGroup from '../PollProgressItemGroup/PollProgressItemGroup';
import { PollInterface, SelectedPollItemInterface } from '../../types/poll';
import PollProgressDetail from '../PollProgressDetail/PollProgressDetail';

interface Props {
  pollId: PollInterface['id'];
}

function PollProgressForm({ pollId }: Props) {
  const navigate = useNavigate();
  const { groupCode, pollId } = useParams();
  // TODO: 기본 객체를 줘야할까? undefined로 놓는 것이 위험한가?
  const [poll, setPoll] = useState<PollInterface>();
  const [selectedPollItems, setSelectedPollItems] = useState<Array<SelectedPollItemInterface>>([]);

  // TODO: 객체로 state로 관리하는 것에 단점이 분명히 있다. 리팩토링 필요
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      if (poll) {
        if (groupCode) {
          await progressPoll(poll.id, selectedPollItems, groupCode);
          navigate(`/groups/${groupCode}/poll/${pollId}/result`);
        }
      }
    } catch (err) {
      alert(err);
    }
  };

  // TODO: 두 가지 역할을 하는 걸까?
  const handleSelectPollItems = (mode: string) => (e: ChangeEvent<HTMLInputElement>) => {
    const id = Number(e.target.id);
    const newSelectedPollItems = JSON.parse(JSON.stringify(selectedPollItems));

    if (mode === 'single') {
      // 디스크립션창을 추가한다
      setSelectedPollItems([{ itemId: id, description: '' }]);

      return;
    }

    if (mode === 'multiple' && e.target.checked) {
      setSelectedPollItems([...newSelectedPollItems, { itemId: id, description: '' }]);

      return;
    }

    setSelectedPollItems(
      [...newSelectedPollItems].filter((selectedPollItem) => selectedPollItem.itemId !== id)
    );
  };

  const handleDescription = (pollId: PollInterface['id']) => (e: ChangeEvent<HTMLInputElement>) => {
    const newSelectedPollItems = JSON.parse(JSON.stringify(selectedPollItems));
    // TODO: for문 개선, 에러 해결
    for (const newSelectedPollItem of newSelectedPollItems) {
      if (newSelectedPollItem.itemId === pollId) {
        newSelectedPollItem.description = e.target.value;
        setSelectedPollItems(newSelectedPollItems);

        return;
      }
    }
  };

  useEffect(() => {
    const fetchPoll = async (pollId: PollInterface['id']) => {
      // res가 있는지?
      try {
        if (groupCode) {
          const res = await getPoll(pollId, groupCode);

          if (res.status === 'CLOSED') {
            navigate(`/groups/${groupCode}/poll`);
          }

          setPoll(res);
        }
        // TODO: pollid가 없을 때 메인 화면으로 보내주기!
      } catch (err) {
        alert('poll 없어~~');
        navigate(`/groups/${groupCode}/poll`);
      }
    };

    if (pollId) {
      fetchPoll(Number(pollId));
    }
  };

  useEffect(() => {
    const fetchPoll = async (pollId: PollInterface['id']) => {
      // res가 있는지?
      try {
        const res = await getPoll(pollId);
        if (res.status === 'CLOSED') {
          navigate('/poll');
        }

        setPoll(res);
        // TODO: pollid가 없을 때 메인 화면으로 보내주기!
      } catch (err) {
        alert('poll 없어~~');
        navigate('/poll');
      }
    };
    fetchPoll(pollId);
  }, []);

  return (
    // TODO: 화면 작았다 켜지는 것 수정
    <Box width="84.4rem" padding="6.4rem 4.8rem 5.4rem 4.8rem">
      {poll && pollId && groupCode ? (
        <form onSubmit={handleSubmit}>
          <MarginContainer margin="0 0 4rem 0">
            <PollTitle title={poll.title} />
            <Divider />
          </MarginContainer>
          <MarginContainer margin="0 0 1.6rem 0">
            <PollProgressDetail
              isAnonymous={poll.isAnonymous}
              allowedPollCount={poll.allowedPollCount}
            />
          </MarginContainer>
          <MarginContainer margin="0 0 8.4rem 0">
            <PollProgressItemGroup
              pollId={poll.id}
              selectedPollItems={selectedPollItems}
              allowedPollCount={poll.allowedPollCount}
              handleSelectPollItems={handleSelectPollItems}
              handleDescription={handleDescription}
              groupCode={groupCode}
            />
          </MarginContainer>
          <PollProgressSubmitButton
            pollId={Number(pollId)}
            isHost={poll.isHost}
            groupCode={groupCode}
          />
        </form>
      ) : (
        <div>로딩중</div>
      )}
    </Box>
  );
}

export default PollProgressForm;
