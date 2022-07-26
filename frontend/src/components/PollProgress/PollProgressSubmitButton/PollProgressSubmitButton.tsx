import React from 'react';
import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import { deletePoll } from '../../../api/poll';
import { PollInterface } from '../../../types/poll';

import Button from '../../common/Button/Button';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import { GroupInterface } from '../../../types/group';

interface Props {
  pollId: PollInterface['id'];
  isHost: PollInterface['isHost'];
  groupCode?: GroupInterface['code'];
}

function PollProgressSubmitButton({ pollId, isHost, groupCode }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleDeletePoll = async () => {
    if (window.confirm('투표를 삭제하시겠습니까?')) {
      try {
        if (groupCode) {
          await deletePoll(pollId, groupCode);
          navigate(`/groups/${groupCode}/poll`);
        }
      } catch (err) {
        alert(err);
      }
    }
  };

  return (
    <FlexContainer justifyContent="center" gap="2rem">
      {isHost ? (
        <Button
          variant="filled"
          width="46rem"
          padding="2rem 0"
          colorScheme={theme.colors.GRAY_400}
          color={theme.colors.WHITE_100}
          fontSize="2rem"
          onClick={handleDeletePoll}
        >
          투표 삭제하기
        </Button>
      ) : (
        ''
      )}
      <Button
        variant="filled"
        width="46rem"
        padding="2rem 0"
        colorScheme={theme.colors.PURPLE_100}
        color={theme.colors.WHITE_100}
        fontSize="2rem"
        type="submit"
      >
        투표하기
      </Button>
    </FlexContainer>
  );
}

export default PollProgressSubmitButton;
