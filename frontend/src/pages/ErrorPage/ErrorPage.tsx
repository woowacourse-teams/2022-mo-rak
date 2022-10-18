import { useTheme } from '@emotion/react';
import { useLottie } from 'lottie-react';
import { useNavigate, useParams } from 'react-router-dom';
import notFound from '../../assets/not-found-animation.json';
import Button from '../../components/Button/Button';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import { StyledGuid, StyledContainer } from './ErrorPage.styles';
import { GroupInterface } from '../../types/group';

function ErrorPage() {
  const theme = useTheme();
  const navigate = useNavigate();
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const notFoundAnimation = useLottie({ animationData: notFound }, { width: '50rem' });

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleNavigate = (location: string) => () => {
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
          onClick={handleGoBack}
        >
          뒤로 가기
        </Button>
        <Button
          variant="filled"
          colorScheme={theme.colors.YELLOW_100}
          padding="1.2rem 3rem"
          fontSize="2.4rem"
          borderRadius="1rem"
          onClick={handleNavigate(`/groups/${groupCode}`)}
        >
          모락으로 돌아가기
        </Button>
      </FlexContainer>
    </StyledContainer>
  );
}

export default ErrorPage;
