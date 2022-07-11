import { useTheme } from '@emotion/react';

import React from 'react';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import Button from '../common/Button/Button';
import { closePoll, deletePoll } from '../../api/poll';
import { PollInterface } from '../../types/poll';

interface Props {
  pollId: PollInterface['id'];
}

function PollResultButtonGroup({ pollId }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleDeletePoll = async () => {
    if (window.confirm('투표를 삭제하시겠습니까?')) {
      try {
        await deletePoll(pollId);
        navigate('/create');
      } catch (err) {
        alert(err);
      }
    }
  };

  const handleClosePoll = async () => {
    if (window.confirm('투표를 마감하시겠습니까?')) {
      try {
        await closePoll(pollId);
      } catch (err) {
        alert(err);
      }
    }
  };

  return (
    <FlexContainer gap="2rem" justifyContent="center">
      <Button
        type="button"
        variant="filled"
        width="15.4rem"
        height="6.4rem"
        fontSize="2rem"
        color={theme.colors.WHITE_100}
        colorScheme={theme.colors.GRAY_400}
        onClick={handleClosePoll}
      >
        투표 마감하기
      </Button>
      <Button
        type="button"
        variant="filled"
        width="15.4rem"
        height="6.4rem"
        fontSize="2rem"
        color={theme.colors.WHITE_100}
        colorScheme={theme.colors.GRAY_400}
        onClick={handleDeletePoll}
      >
        투표 삭제하기
      </Button>
      <Button
        type="button"
        variant="filled"
        width="15.4rem"
        height="6.4rem"
        fontSize="2rem"
        color={theme.colors.WHITE_100}
        colorScheme={theme.colors.PURPLE_100}
        onClick={() => navigate('/progress')}
      >
        재투표하기
      </Button>
    </FlexContainer>
  );
}

export default PollResultButtonGroup;
