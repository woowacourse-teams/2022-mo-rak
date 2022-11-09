import { useTheme } from '@emotion/react';

import { useNavigate } from 'react-router-dom';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import Button from '@/components/Button/Button';
import { closePoll, deletePoll } from '@/api/poll';
import { Poll, getPollResponse, getPollItemsResponse } from '@/types/poll';
import { Group } from '@/types/group';
import { AxiosError } from 'axios';

type Props = {
  pollCode: Poll['code'];
  status: Poll['status'];
  setStatus: (status: Poll['status']) => void;
  isHost: getPollResponse['isHost'];
  groupCode: Group['code'];
  pollItems: getPollItemsResponse;
};

function PollResultButtons({ pollCode, status, setStatus, isHost, groupCode, pollItems }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();
  const isProgressedPoll = pollItems.some((pollItem) => pollItem.isSelected);
  const progressButtonMessage = isProgressedPoll ? '재투표하기' : '투표하기';

  const handleDeletePoll = async () => {
    if (window.confirm('투표를 삭제하시겠습니까?')) {
      try {
        await deletePoll(pollCode, groupCode);
        navigate(`/groups/${groupCode}/poll`);
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;

          if (errCode === '2300') {
            alert('존재하지 않는 투표입니다');
            navigate(`/groups/${groupCode}/poll`);
          }
        }
      }
    }
  };

  const handleClosePoll = async () => {
    if (window.confirm('투표를 마감하시겠습니까?')) {
      try {
        await closePoll(pollCode, groupCode);
        setStatus('CLOSED');
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;

          if (errCode === '2100') {
            alert('이미 마감된 투표입니다');
            setStatus('CLOSED');
          }
        }
      }
    }
  };

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  if (isHost) {
    return (
      <>
        {status === 'OPEN' && (
          <FlexContainer gap="2rem" justifyContent="space-between">
            <Button
              variant="filled"
              width="calc(100% / 3)"
              padding="2rem 0"
              fontSize="2rem"
              colorScheme={theme.colors.GRAY_400}
              onClick={handleClosePoll}
            >
              투표 마감하기
            </Button>
            <Button
              variant="filled"
              width="calc(100% / 3)"
              padding="2rem 0"
              fontSize="2rem"
              colorScheme={theme.colors.GRAY_400}
              onClick={handleDeletePoll}
            >
              투표 삭제하기
            </Button>
            <Button
              variant="filled"
              width="calc(100% / 3)"
              padding="2rem 0"
              fontSize="2rem"
              colorScheme={theme.colors.PURPLE_100}
              onClick={handleNavigate(`/groups/${groupCode}/poll/${pollCode}/progress`)}
            >
              {progressButtonMessage}
            </Button>
          </FlexContainer>
        )}

        {status === 'CLOSED' && (
          <FlexContainer justifyContent="center">
            <Button
              type="button"
              variant="filled"
              width="22.8rem"
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
            width="22.8rem"
            padding="2rem 0"
            fontSize="2rem"
            colorScheme={theme.colors.PURPLE_100}
            onClick={handleNavigate(`/groups/${groupCode}/poll/${pollCode}/progress`)}
          >
            {progressButtonMessage}
          </Button>
        </FlexContainer>
      )}
    </>
  );
}

export default PollResultButtons;
