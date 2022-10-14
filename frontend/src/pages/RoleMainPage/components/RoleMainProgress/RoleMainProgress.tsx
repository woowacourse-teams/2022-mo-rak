import { useState } from 'react';
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

function RoleMainProgress() {
  const theme = useTheme();
  const [isLottieVisible, setIsLottieVisible] = useState(false);
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

  const handleTriggerAnimation = () => {
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

  return (
    <>
      {/* 역할칩 */}
      <StyledRolesContainer>
        {/* 이후 api에서 받아서 map으로 실제 데이터 넣어주기 */}
        <StyledRoleWrapper>
          <TextField
            padding="1.8rem 2.6rem"
            borderRadius="40px"
            variant="outlined"
            colorScheme={theme.colors.PURPLE_100}
          >
            <StyledDetail>데일리 마스터</StyledDetail>
          </TextField>
        </StyledRoleWrapper>

        <StyledRoleWrapper>
          <TextField
            padding="1.8rem 2.6rem"
            borderRadius="40px"
            variant="outlined"
            colorScheme={theme.colors.PURPLE_100}
          >
            <StyledDetail>데일리 마스터</StyledDetail>
          </TextField>
        </StyledRoleWrapper>

        <StyledRoleWrapper>
          <TextField
            padding="1.8rem 2.6rem"
            borderRadius="40px"
            variant="outlined"
            colorScheme={theme.colors.PURPLE_100}
          >
            <StyledDetail>데일리 마스터</StyledDetail>
          </TextField>
        </StyledRoleWrapper>

        <StyledRoleWrapper>
          <TextField
            padding="1.8rem 2.6rem"
            borderRadius="40px"
            variant="outlined"
            colorScheme={theme.colors.PURPLE_100}
          >
            <StyledDetail>데일리 마스터</StyledDetail>
          </TextField>
        </StyledRoleWrapper>

        <StyledRoleWrapper>
          <TextField
            padding="1.8rem 2.6rem"
            borderRadius="40px"
            variant="outlined"
            colorScheme={theme.colors.PURPLE_100}
          >
            <StyledDetail>데일리 마스터</StyledDetail>
          </TextField>
        </StyledRoleWrapper>

        <StyledRoleWrapper>
          <TextField
            padding="1.8rem 2.6rem"
            borderRadius="40px"
            variant="outlined"
            colorScheme={theme.colors.PURPLE_100}
          >
            <StyledDetail>데일리 마스터</StyledDetail>
          </TextField>
        </StyledRoleWrapper>

        <StyledRoleWrapper>
          <TextField
            padding="1.8rem 2.6rem"
            borderRadius="40px"
            variant="outlined"
            colorScheme={theme.colors.PURPLE_100}
          >
            <StyledDetail>데일리 마스터</StyledDetail>
          </TextField>
        </StyledRoleWrapper>
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
          onClick={handleTriggerAnimation}
        >
          역할 정하기
        </Button>
        <StyledLottieWrapper isVisible={isLottieVisible}>{fireworkLottie.View}</StyledLottieWrapper>
      </FlexContainer>
      <RoleMainRoleEditModal isVisible={isRoleEditModalVisible} close={handleCloseRoleEditModal} />
    </>
  );
}

export default RoleMainProgress;
