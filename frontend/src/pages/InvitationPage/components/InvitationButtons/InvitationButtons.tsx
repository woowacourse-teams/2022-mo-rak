import { NavigateFunction } from 'react-router-dom';
import { useTheme } from '@emotion/react';
import Button from '@/components/Button/Button';
import FlexContainer from '@/components/FlexContainer/FlexContainer';

import { participateGroup } from '@/apis/group';
import { Group } from '@/types/group';
import { AxiosError } from 'axios';

type Props = {
  navigate: NavigateFunction;
  invitationCode: string;
  groupCode: Group['code'];
};

function InvitationButtons({ navigate, invitationCode, groupCode }: Props) {
  const theme = useTheme();

  const handleParticipateGroup = async () => {
    try {
      await participateGroup(invitationCode);
      navigate(`/groups/${groupCode}`);
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '1101') {
          alert('이미 가입된 모임입니다~');
          navigate(`/groups/${groupCode}`);
        }
      }
    }
  };

  const handleCancelInvitation = () => {
    if (window.confirm('거절하시겠습니까?')) {
      navigate('/');
    }
  };

  return (
    <FlexContainer gap="1.2rem">
      <Button
        variant="filled"
        colorScheme={theme.colors.PURPLE_100}
        width="18.4rem"
        fontSize="2.4rem"
        padding="2rem 0"
        onClick={handleParticipateGroup}
      >
        수락
      </Button>
      <Button
        variant="filled"
        colorScheme={theme.colors.GRAY_400}
        width="18.4rem"
        fontSize="2.4rem"
        padding="2rem 0"
        onClick={handleCancelInvitation}
      >
        거절
      </Button>
    </FlexContainer>
  );
}

export default InvitationButtons;
