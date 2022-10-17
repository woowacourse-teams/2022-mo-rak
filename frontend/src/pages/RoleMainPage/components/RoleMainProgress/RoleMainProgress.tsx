import { useEffect, useState } from 'react';

import { useTheme } from '@emotion/react';
import Button from '../../../../components/Button/Button';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import TextField from '../../../../components/TextField/TextField';
import {
  StyledDetail,
  StyledRolesContainer,
  StyledRoleContainer,
  StyledLottieContainer
} from './RoleMainProgress.styles';
import { useLottie } from 'lottie-react';
import firework from '../../../../assets/firework-animation.json';
import RoleMainRoleEditModal from '../RoleMainRoleEditModal/RoleMainRoleEditModal';
import { allocateRoles, getRoles } from '../../../../api/role';
import { useParams } from 'react-router-dom';
import { GroupInterface } from '../../../../types/group';
import { EditRolesRequest } from '../../../../types/role';
import { AxiosError } from 'axios';

type Props = {
  onClickAllocateRolesButton: () => void;
};

function RoleMainProgress({ onClickAllocateRolesButton }: Props) {
  const theme = useTheme();
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const [roles, setRoles] = useState<EditRolesRequest['roles']>([]);
  const [isFireworkAnimationVisible, setIsFireworkAnimationVisible] = useState(false);
  const [isRoleEditModalVisible, setIsRoleEditModalVisible] = useState(false);
  const fireworkAnimation = useLottie(
    {
      animationData: firework,
      autoplay: false
    },
    {
      width: '68rem'
    }
  );

  const triggerFireworkAnimation = () => {
    setIsFireworkAnimationVisible(true);
    fireworkAnimation.play();

    setTimeout(() => {
      setIsFireworkAnimationVisible(false);
      fireworkAnimation.pause();
    }, 2000);
  };

  const handleAllocateRoles = async () => {
    try {
      await allocateRoles(groupCode);
      triggerFireworkAnimation();
      onClickAllocateRolesButton();
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        switch (errCode) {
          case '5101': {
            alert('역할의 개수는 1개 이상, 100개 이하여야합니다');

            break;
          }

          case '5102': {
            alert('역할은 팀 멤버 수 이하여야합니다');

            break;
          }
        }
      }
    }
  };

  const handleCloseRoleEditModal = () => {
    setIsRoleEditModalVisible(false);
  };

  const handleOpenRoleEditModal = () => {
    setIsRoleEditModalVisible(true);
  };

  const fetchRoles = async () => {
    try {
      const res = await getRoles(groupCode);
      setRoles(res.data.roles);
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;
        if (errCode === '5300') {
          alert('역할이 존재하지 않습니다');
        }
      }
    }
  };

  useEffect(() => {
    fetchRoles();
  }, []);

  return (
    <>
      <StyledRolesContainer>
        {roles.map((role, idx) => {
          return (
            <StyledRoleContainer key={`${idx}-${role}`}>
              <TextField
                padding="1.8rem 2.6rem"
                borderRadius="40px"
                variant="outlined"
                colorScheme={theme.colors.PURPLE_100}
              >
                <StyledDetail>{role}</StyledDetail>
              </TextField>
            </StyledRoleContainer>
          );
        })}
      </StyledRolesContainer>

      <FlexContainer justifyContent="center" gap="2rem">
        <Button
          variant="filled"
          width="16rem"
          padding="1.6rem 0"
          fontSize="2rem"
          borderRadius="12px"
          colorScheme={theme.colors.GRAY_300}
          onClick={handleOpenRoleEditModal}
        >
          역할 수정하기
        </Button>
        <Button
          variant="filled"
          width="16rem"
          padding="1.6rem 0"
          fontSize="2rem"
          borderRadius="12px"
          colorScheme={theme.colors.PURPLE_100}
          onClick={handleAllocateRoles}
        >
          역할 정하기
        </Button>
        <StyledLottieContainer isVisible={isFireworkAnimationVisible}>
          {fireworkAnimation.View}
        </StyledLottieContainer>
      </FlexContainer>
      {isRoleEditModalVisible && (
        <RoleMainRoleEditModal
          close={handleCloseRoleEditModal}
          initialRoles={roles}
          onEditRoles={fetchRoles}
        />
      )}
    </>
  );
}

export default RoleMainProgress;
