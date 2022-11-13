import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import { deletePoll } from '@/apis/poll';
import { Poll, getPollResponse } from '@/types/poll';
import Button from '@/components/Button/Button';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import { Group } from '@/types/group';
import { AxiosError } from 'axios';

type Props = {
  pollCode: Poll['code'];
  isHost: getPollResponse['isHost'];
  groupCode: Group['code'];
};

function PollProgressButtons({ pollCode, isHost, groupCode }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleDeletePoll = async () => {
    if (window.confirm('투표를 삭제하시겠습니까?')) {
      try {
        await deletePoll(pollCode, groupCode);
        navigate(`/groups/${groupCode}/poll`);
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;

          if (errCode === '2300') {
            alert('존재하지 않는 투표입니다!');

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
