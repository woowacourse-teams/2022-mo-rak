/* eslint-disable react/jsx-no-useless-fragment */
import { useTheme } from '@emotion/react';

import React from 'react';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Button from '../../@common/Button/Button';
import { closePoll, deletePoll } from '../../../api/poll';
import { PollInterface, getPollResponse } from '../../../types/poll';
import { GroupInterface } from '../../../types/group';

interface Props {
  pollCode: PollInterface['code'];
  status: PollInterface['status'];
  isHost: getPollResponse['isHost'];
  groupCode: GroupInterface['code'];
}

function PollResultButtonGroup({ pollCode, status, isHost, groupCode }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleDeletePoll = async () => {
    if (window.confirm('투표를 삭제하시겠습니까?')) {
      try {
        await deletePoll(pollCode, groupCode);
        navigate(`/groups/${groupCode}/poll`);
      } catch (err) {
        alert(err);
      }
    }
  };

  const handleClosePoll = async () => {
    if (window.confirm('투표를 마감하시겠습니까?')) {
      try {
        await closePoll(pollCode, groupCode);
        navigate(`/groups/${groupCode}/poll`);
      } catch (err) {
        alert(err);
      }
    }
  };

  const handleNavigate = (path: string) => () => {
    navigate(path);
  };

  if (isHost) {
    return (
      <>
        {status === 'OPEN' ? (
          <FlexContainer gap="2rem" justifyContent="center">
            <Button
              type="button"
              variant="filled"
              width="15.4rem"
              padding="2rem 0"
              fontSize="2rem"
              colorScheme={theme.colors.GRAY_400}
              onClick={handleClosePoll}
            >
              투표 마감하기
            </Button>
            <Button
              type="button"
              variant="filled"
              width="15.4rem"
              padding="2rem 0"
              fontSize="2rem"
              colorScheme={theme.colors.GRAY_400}
              onClick={handleDeletePoll}
            >
              투표 삭제하기
            </Button>
            <Button
              type="button"
              variant="filled"
              width="15.4rem"
              padding="2rem 0"
              fontSize="2rem"
              colorScheme={theme.colors.PURPLE_100}
              onClick={handleNavigate(`/groups/${groupCode}/poll/${pollCode}/progress`)}
            >
              재투표하기
            </Button>
          </FlexContainer>
        ) : (
          <FlexContainer justifyContent="center">
            <Button
              type="button"
              variant="filled"
              width="32.4rem"
              padding="2rem 0"
              fontSize="2rem"
              colorScheme={theme.colors.GRAY_400}
              onClick={handleDeletePoll}
            >
              투표 삭제하기
            </Button>
          </FlexContainer>
        )}
      </>
    );
  }

  return (
    <>
      {status === 'OPEN' && (
        <FlexContainer gap="2rem" justifyContent="center">
          <Button
            type="button"
            variant="filled"
            width="15.4rem"
            padding="2rem 0"
            fontSize="2rem"
            colorScheme={theme.colors.PURPLE_100}
            onClick={handleNavigate(`/groups/${groupCode}/poll/${pollCode}/progress`)}
          >
            재투표하기
          </Button>
        </FlexContainer>
      )}
    </>
  );
}

export default PollResultButtonGroup;
