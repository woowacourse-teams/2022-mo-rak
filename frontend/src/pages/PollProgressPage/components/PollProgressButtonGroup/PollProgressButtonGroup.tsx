import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import { deletePoll } from '../../../../api/poll';
import { PollInterface, getPollResponse } from '../../../../types/poll';
import Button from '../../../../components/Button/Button';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { GroupInterface } from '../../../../types/group';

interface Props {
  pollCode: PollInterface['code'];
  isHost: getPollResponse['isHost'];
  groupCode: GroupInterface['code'];
}

// TODO: 삭제랑, 투표하기 버튼이 둘 다 있어서 Button Group 해야할듯?
function PollProgressButtonGroup({ pollCode, isHost, groupCode }: Props) {
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

  return (
    <FlexContainer justifyContent="center" gap="2rem">
      {isHost && (
        <Button
          variant="filled"
          width="46rem"
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
        width="46rem"
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

export default PollProgressButtonGroup;
