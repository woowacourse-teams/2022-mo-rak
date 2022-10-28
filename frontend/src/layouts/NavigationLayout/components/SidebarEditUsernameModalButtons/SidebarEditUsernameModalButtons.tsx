import { useTheme } from '@emotion/react';
import { StyledContainer } from './SidebarEditUsernameModalButtons.styles';
import Button from '../../../../components/Button/Button';

type Props = {
  onClickCancelButton: () => void;
};

function SidebarEditUsernameModalButtons({ onClickCancelButton }: Props) {
  const theme = useTheme();

  return (
    <StyledContainer>
      <Button
        variant="filled"
        colorScheme={theme.colors.GRAY_400}
        width="50%"
        padding="1.6rem 3.2rem"
        borderRadius="1.2rem"
        fontSize="1.6rem"
        onClick={onClickCancelButton}
      >
        취소
      </Button>
      <Button
        variant="filled"
        colorScheme={theme.colors.YELLOW_200}
        padding="1.6rem 3.2rem"
        width="50%"
        borderRadius="1.2rem"
        fontSize="1.6rem"
        type="submit"
      >
        변경
      </Button>
    </StyledContainer>
  );
}

export default SidebarEditUsernameModalButtons;
