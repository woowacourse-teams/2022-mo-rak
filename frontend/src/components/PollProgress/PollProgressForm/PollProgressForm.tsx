import React, { ChangeEvent, FormEvent, useEffect, useState } from 'react';

import { useNavigate, useParams } from 'react-router-dom';
import styled from '@emotion/styled';
import Box from '../../@common/Box/Box';
import Divider from '../../@common/Divider/Divider';
import MarginContainer from '../../@common/MarginContainer/MarginContainer';
import PollProgressButtonGroup from '../PollProgressButtonGroup/PollProgressButtonGroup';
import { getPoll, progressPoll } from '../../../api/poll';
import PollProgressItemGroup from '../PollProgressItemGroup/PollProgressItemGroup';
import { PollInterface, SelectedPollItem, getPollResponse } from '../../../types/poll';
import PollProgressDetail from '../PollProgressDetail/PollProgressDetail';
import { GroupInterface } from '../../../types/group';

function PollProgressForm() {
  const navigate = useNavigate();
  const { groupCode, pollCode } = useParams() as {
    groupCode: GroupInterface['code'];
    pollCode: PollInterface['code'];
  };
  const [poll, setPoll] = useState<getPollResponse>();
  const [selectedPollItems, setSelectedPollItems] = useState<Array<SelectedPollItem>>([]);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    if (!poll) return;

    e.preventDefault();

    try {
      await progressPoll(pollCode, selectedPollItems, groupCode);
      navigate(`/groups/${groupCode}/poll/${pollCode}/result`);
    } catch (err) {
      alert(err);
    }
  };

  const handleSelectPollItems = (e: ChangeEvent<HTMLInputElement>) => {
    const id = Number(e.target.id);
    // TODO: 깊은 복사 함수 만들어보기!
    const newSelectedPollItems = JSON.parse(JSON.stringify(selectedPollItems));

    if (e.target.checked) {
      setSelectedPollItems([...newSelectedPollItems, { itemId: id, description: '' }]);

      return;
    }

    setSelectedPollItems(
      [...newSelectedPollItems].filter((selectedPollItem) => selectedPollItem.itemId !== id)
    );
  };

  const handleSelectPollItem = (e: ChangeEvent<HTMLInputElement>) => {
    const id = Number(e.target.id);

    setSelectedPollItems([{ itemId: id, description: '' }]);
  };

  const handleDescription = (pollId: PollInterface['id']) => (e: ChangeEvent<HTMLInputElement>) => {
    // TODO: 깊은 복사 함수 만들기
    const newSelectedPollItems: Array<SelectedPollItem> = JSON.parse(
      JSON.stringify(selectedPollItems)
    );

    const targetPollItem = newSelectedPollItems.find(
      (newSelectedPollItem) => newSelectedPollItem.itemId === pollId
    );

    if (targetPollItem) {
      targetPollItem.description = e.target.value;
      setSelectedPollItems(newSelectedPollItems);
    }
  };

  useEffect(() => {
    const fetchPoll = async (pollCode: PollInterface['code']) => {
      try {
        const res = await getPoll(pollCode, groupCode);

        // TODO: res.data vs poll => 이후에 통일해줘야함
        if (res.data.status === 'CLOSED') {
          navigate(`/groups/${groupCode}/poll`);

          return;
        }

        setPoll(res.data);
      } catch (err) {
        alert('poll 없어~~');
        navigate(`/groups/${groupCode}/poll`);
      }
    };

    fetchPoll(pollCode);
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
              pollCode={poll.code}
              selectedPollItems={selectedPollItems}
              allowedPollCount={poll.allowedPollCount}
              onChangeCheckbox={handleSelectPollItems}
              onChangeRadio={handleSelectPollItem}
              onChangeText={handleDescription}
              groupCode={groupCode}
            />
          </MarginContainer>
          <PollProgressButtonGroup pollCode={pollCode} isHost={poll.isHost} groupCode={groupCode} />
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
