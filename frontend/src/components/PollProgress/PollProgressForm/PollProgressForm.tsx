import React, { ChangeEvent, FormEvent, useEffect, useState } from 'react';

import { useNavigate, useParams } from 'react-router-dom';
import styled from '@emotion/styled';
import Box from '../../common/Box/Box';
import Divider from '../../common/Divider/Divider';
import MarginContainer from '../../common/MarginContainer/MarginContainer';

import PollProgressSubmitButton from '../PollProgressSubmitButton/PollProgressSubmitButton';
import { getPoll, progressPoll } from '../../../api/poll';
import PollProgressItemGroup from '../PollProgressItemGroup/PollProgressItemGroup';
import { PollInterface, SelectedPollItemInterface } from '../../../types/poll';
import PollProgressDetail from '../PollProgressDetail/PollProgressDetail';

function PollProgressForm() {
  const navigate = useNavigate();
  const { groupCode, pollId } = useParams() as {
    groupCode: string;
    pollId: string;
  };
  // TODO: 기본 객체를 줘야할까? undefined로 놓는 것이 위험한가?
  const [poll, setPoll] = useState<PollInterface>();
  const [selectedPollItems, setSelectedPollItems] = useState<Array<SelectedPollItemInterface>>([]);

  // TODO: 객체로 state를 관리하는 것에 단점이 분명히 있다. 리팩토링 필요 usereducer 찾아보자
  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      if (poll) {
        await progressPoll(poll.id, selectedPollItems, groupCode);
        navigate(`/groups/${groupCode}/poll/${pollId}/result`);
      }
    } catch (err) {
      alert(err);
    }
  };

  // TODO: 두 가지 역할을 하는 걸까? 나중에 시간 있을 때 하기~
  const handleSelectPollItems = (mode: string) => (e: ChangeEvent<HTMLInputElement>) => {
    const id = Number(e.target.id);
    // TODO: 깊은 복사 함수 만들어보기!
    const newSelectedPollItems = JSON.parse(JSON.stringify(selectedPollItems));

    if (mode === 'single') {
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
      try {
        const res = await getPoll(pollId, groupCode);

        if (res.status === 'CLOSED') {
          navigate(`/groups/${groupCode}/poll`);

          return;
        }

        setPoll(res);
      } catch (err) {
        alert('poll 없어~~');
        navigate(`/groups/${groupCode}/poll`);
      }
    };

    fetchPoll(Number(pollId));
  }, []);

  return (
    <Box width="84.4rem" padding="6.4rem 4.8rem 5.4rem 4.8rem">
      {poll ? (
        <form onSubmit={handleSubmit}>
          <MarginContainer margin="0 0 4rem 0">
            <StyledTitle>{poll.title}</StyledTitle>
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

const StyledTitle = styled.h1(
  ({ theme }) => `
  color: ${theme.colors.BLACK_100};
  font-size: 3.2rem;
`
);

export default PollProgressForm;
