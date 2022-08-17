import React from 'react';

import { NavigateFunction } from 'react-router-dom';
import { useTheme } from '@emotion/react';
import Button from '../../@common/Button/Button';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';

import { participateGroup } from '../../../api/group';
import { GroupInterface } from '../../../types/group';

interface Props {
  navigate: NavigateFunction;
  invitationCode: string;
  groupCode: GroupInterface['code'];
}

function InvitationButtonGroup({ navigate, invitationCode, groupCode }: Props) {
  const theme = useTheme();

  const handleParticipateGroup = async () => {
    try {
      await participateGroup(invitationCode);
      navigate(`/groups/${groupCode}`);
    } catch (err) {
      if (err instanceof Error) {
        console.log(JSON.stringify(err));
      }
    }
  };

  const handleCancelInvitation = () => {
    if (window.confirm('거절하시겠습니까?')) {
      console.log('취소');
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

export default InvitationButtonGroup;
