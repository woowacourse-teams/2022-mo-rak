import { ChangeEvent, FormEvent, useEffect, useState } from 'react';

import { useNavigate, useParams } from 'react-router-dom';
import styled from '@emotion/styled';
import Box from '../../../../components/Box/Box';
import Divider from '../../../../components/Divider/Divider';
import MarginContainer from '../../../../components/MarginContainer/MarginContainer';
import PollProgressButtonGroup from '../PollProgressButtonGroup/PollProgressButtonGroup';
import { getPoll, progressPoll, getPollItems } from '../../../../api/poll';
import PollProgressItemGroup from '../PollProgressItemGroup/PollProgressItemGroup';
import {
  PollInterface,
  SelectedPollItem,
  getPollResponse,
  getPollItemsResponse,
  PollItemInterface
} from '../../../../types/poll';
import PollProgressDetail from '../PollProgressDetail/PollProgressDetail';
import { GroupInterface } from '../../../../types/group';

const getInitialSelectedPollItems = (pollItems: getPollItemsResponse) =>
  pollItems
    .filter((pollItem) => pollItem.isSelected)
    .map((pollItem) => ({
      id: pollItem.id,
      description: pollItem.description
    }));

function PollProgressForm() {
  const navigate = useNavigate();
  const { groupCode, pollCode } = useParams() as {
    groupCode: GroupInterface['code'];
    pollCode: PollInterface['code'];
  };
  const [poll, setPoll] = useState<getPollResponse>();
  const [pollItems, setPollItems] = useState<getPollItemsResponse>([]);
  const [selectedPollItems, setSelectedPollItems] = useState<Array<SelectedPollItem>>([]);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!poll) return;
    if (selectedPollItems.length <= 0) {
      alert('최소 1개의 선택항목을 선택해주세요!');

      return;
    }

    try {
      await progressPoll(pollCode, selectedPollItems, groupCode);
      navigate(`/groups/${groupCode}/poll/${pollCode}/result`);
    } catch (err) {
      alert(err);
    }
  };

  const handleSelectPollItems = (e: ChangeEvent<HTMLInputElement>) => {
    const id = Number(e.target.id);
    const isSelected = e.target.checked;
    // TODO: 깊은 복사 함수 만들어보기!
    const copiedSelectedPollItems: Array<SelectedPollItem> = JSON.parse(
      JSON.stringify(selectedPollItems)
    );

    if (isSelected) {
      // NOTE: EMPTY_STRING 상수화 어떤가요?
      setSelectedPollItems([...copiedSelectedPollItems, { id, description: '' }]);

      return;
    }

    setSelectedPollItems(
      copiedSelectedPollItems.filter((selectedPollItem) => selectedPollItem.id !== id)
    );
  };

  const handleSelectPollItem = (e: ChangeEvent<HTMLInputElement>) => {
    const id = Number(e.target.id);

    setSelectedPollItems([{ id, description: '' }]);
  };

  const handleDescription =
    (pollItemId: PollItemInterface['id']) => (e: ChangeEvent<HTMLInputElement>) => {
      // TODO: 깊은 복사 함수 만들기
      const copiedSelectedPollItems: Array<SelectedPollItem> = JSON.parse(
        JSON.stringify(selectedPollItems)
      );

      const targetPollItem = copiedSelectedPollItems.find(
        (copiedSelectedPollItem) => copiedSelectedPollItem.id === pollItemId
      );

      if (targetPollItem) {
        targetPollItem.description = e.target.value;
        setSelectedPollItems(copiedSelectedPollItems);
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

  useEffect(() => {
    const fetchPollItems = async (pollCode: PollInterface['code']) => {
      try {
        const res = await getPollItems(pollCode, groupCode);
        setPollItems(res.data);
      } catch (err) {
        alert(err);
      }
    };

    fetchPollItems(pollCode);
  }, []);

  useEffect(() => {
    const initialSelectedPollItems = getInitialSelectedPollItems(pollItems);

    setSelectedPollItems(initialSelectedPollItems);
  }, [pollItems]);

  return (
    <Box width="84.4rem" padding="6.4rem 4.8rem">
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
          <MarginContainer margin="0 0 23.6rem 0">
            <PollProgressItemGroup
              pollItems={pollItems}
              selectedPollItems={selectedPollItems}
              allowedPollCount={poll.allowedPollCount}
              onChangeCheckbox={handleSelectPollItems}
              onChangeRadio={handleSelectPollItem}
              onChangeText={handleDescription}
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
