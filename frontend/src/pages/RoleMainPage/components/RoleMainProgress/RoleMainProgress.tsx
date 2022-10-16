import { useEffect, useState } from 'react';

import { useTheme } from '@emotion/react';
import Button from '../../../../components/Button/Button';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import TextField from '../../../../components/TextField/TextField';
import {
  StyledDetail,
  StyledRolesContainer,
  StyledRoleWrapper,
  StyledLottieWrapper
} from './RoleMainProgress.styles';
import { useLottie } from 'lottie-react';
import fireworkAnimation from '../../../../assets/firework-animation.json';
import RoleMainRoleEditModal from '../RoleMainRoleEditModal/RoleMainRoleEditModal';
import { allocateRoles, getRoles, getRolesHistories } from '../../../../api/role';
import { useParams } from 'react-router-dom';
import { GroupInterface } from '../../../../types/group';
import { EditRolesRequest } from '../../../../types/role';
import { AxiosError } from 'axios';

type Props = {
  onAllocateRoles: () => void;
};

function RoleMainProgress({ onAllocateRoles }: Props) {
  const theme = useTheme();
  const [roles, setRoles] = useState<EditRolesRequest['roles']>([]);
  const [isLottieVisible, setIsLottieVisible] = useState(false);
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const [isRoleEditModalVisible, setIsRoleEditModalVisible] = useState(false);
  const fireworkLottie = useLottie(
    {
      animationData: fireworkAnimation,
      autoplay: false
    },
    {
      width: '68rem'
    }
  );

  const handleAllocateRoles = async () => {
    try {
      await allocateRoles(groupCode);
      triggerFireworkLottie();
      onAllocateRoles();
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

  const triggerFireworkLottie = () => {
    setIsLottieVisible(true);
    fireworkLottie.play();

    setTimeout(() => {
      setIsLottieVisible(false);
      fireworkLottie.pause();
    }, 2000);
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
      {/* 역할칩 */}
      <StyledRolesContainer>
        {roles.map((role, idx) => {
          return (
            <StyledRoleWrapper key={`${idx}-${role}`}>
              <TextField
                padding="1.8rem 2.6rem"
                borderRadius="40px"
                variant="outlined"
                colorScheme={theme.colors.PURPLE_100}
              >
                <StyledDetail>{role}</StyledDetail>
              </TextField>
            </StyledRoleWrapper>
          );
        })}
      </StyledRolesContainer>

      {/* 버튼 */}
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
        <StyledLottieWrapper isVisible={isLottieVisible}>{fireworkLottie.View}</StyledLottieWrapper>
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
