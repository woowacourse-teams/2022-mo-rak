import { useTheme } from '@emotion/react';
import { useParams } from 'react-router-dom';
import Button from '@/components/Button/Button';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import { StyledAnimationContainer } from '@/pages/RoleMainPage/components/RoleMainButtons/RoleMainButtons.styles';
import { Group } from '@/types/group';
import { useState } from 'react';
import { useLottie } from 'lottie-react';
import { allocateRoles } from '@/api/role';
import { AxiosError } from 'axios';
import fireworkLottie from '@/assets/firework-animation.json';
import { ROLE_ERROR } from '@/constants/errorMessage';

type Props = {
  onClickAllocateRolesButton: () => void;
  onClickEditRolesButton: () => void;
};

function RoleMainButtons({ onClickAllocateRolesButton, onClickEditRolesButton }: Props) {
  const theme = useTheme();
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const [isFireworkAnimationVisible, setIsFireworkAnimationVisible] = useState(false);
  const fireworkAnimation = useLottie(
    {
      animationData: fireworkLottie,
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
            alert(ROLE_ERROR.INVALID_COUNT);

            break;
          }

          case '5102': {
            alert(ROLE_ERROR.EXCEED_MEMBER_COUNT);

            break;
          }
        }
      }
    }
  };

  return (
    <FlexContainer justifyContent="center" gap="2rem">
      <Button
        variant="filled"
        width="16rem"
        padding="1.6rem 0"
        fontSize="2rem"
        borderRadius="1.2rem"
        colorScheme={theme.colors.GRAY_300}
        onClick={onClickEditRolesButton}
      >
        역할 수정하기
      </Button>
      <Button
        variant="filled"
        width="16rem"
        padding="1.6rem 0"
        fontSize="2rem"
        borderRadius="1.2rem"
        colorScheme={theme.colors.PURPLE_100}
        onClick={handleAllocateRoles}
      >
        역할 정하기
      </Button>
      <StyledAnimationContainer isVisible={isFireworkAnimationVisible}>
        {fireworkAnimation.View}
      </StyledAnimationContainer>
    </FlexContainer>
  );
}

export default RoleMainButtons;
