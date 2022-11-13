import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import { deletePoll } from '@/api/poll';
import { Poll, getPollResponse } from '@/types/poll';
import Button from '@/components/Button/Button';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import { Group } from '@/types/group';
import { AxiosError } from 'axios';
import { POLL_ERROR } from '@/constants/errorMessage';
import { CONFIRM_MESSAGE } from '@/constants/message';

type Props = {
  pollCode: Poll['code'];
  isHost: getPollResponse['isHost'];
  groupCode: Group['code'];
};

function PollProgressButtons({ pollCode, isHost, groupCode }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleDeletePoll = async () => {
    if (window.confirm(CONFIRM_MESSAGE.DELETE_POLL)) {
      try {
        await deletePoll(pollCode, groupCode);
        navigate(`/groups/${groupCode}/poll`);
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;

          if (errCode === '2300') {
            alert(POLL_ERROR.NOT_EXIST);

            navigate(`/groups/${groupCode}/poll`);
          }
        }
      }
    }
  };

  return (
    <FlexContainer justifyContent="center" gap="2rem">
      {isHost && (
        <Button
          variant="filled"
          width="50%"
          padding="2rem 0"
          colorScheme={theme.colors.GRAY_400}
          fontSize="2rem"
          onClick={handleDeletePoll}
        >
          투표 삭제하기
        </Button>
      )}
      <Button
        variant="filled"
        width="50%"
        padding="2rem 0"
        colorScheme={theme.colors.PURPLE_100}
        fontSize="2rem"
        type="submit"
      >
        투표하기
      </Button>
    </FlexContainer>
  );
}

export default PollProgressButtons;
