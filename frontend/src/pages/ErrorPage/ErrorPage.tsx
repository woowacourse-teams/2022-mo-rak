import { useTheme } from '@emotion/react';
import { useLottie } from 'lottie-react';
import { useNavigate } from 'react-router-dom';
import notFound from '../../assets/not-found-animation.json';
import Button from '../../components/Button/Button';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import { StyledGuid, StyledContainer } from './ErrorPage.styles';

type NavigateFunctionOverload = {
  (location: string): () => void;
  (location: number): () => void;
};

function ErrorPage() {
  const theme = useTheme();
  const navigate = useNavigate();
  const notFoundAnimation = useLottie({ animationData: notFound }, { width: '40rem' });

  const handleNavigate: NavigateFunctionOverload = (location: any) => () => {
    navigate(location);
  };

  return (
    <StyledContainer>
      <div>{notFoundAnimation.View}</div>
      <StyledGuid>
        찾을 수 없는 페이지입니다.
        <br />
        요청하신 페이지가 사라졌거나, 잘못된 경로로 들어오셨어요 :)
      </StyledGuid>
      <FlexContainer gap="1.2rem">
        <Button
          variant="filled"
          colorScheme={theme.colors.GRAY_300}
          padding="1.6rem 3rem"
          fontSize="2.4rem"
          borderRadius="1rem"
          onClick={handleNavigate(-1)}
        >
          뒤로 가기
        </Button>
        <Button
          variant="filled"
          colorScheme={theme.colors.YELLOW_100}
          padding="1.2rem 3rem"
          fontSize="2.4rem"
          borderRadius="1rem"
          onClick={handleNavigate('/')}
        >
          메인으로 돌아가기
        </Button>
      </FlexContainer>
    </StyledContainer>
  );
}

export default ErrorPage;
