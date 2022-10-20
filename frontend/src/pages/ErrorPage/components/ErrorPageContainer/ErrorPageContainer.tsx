import { useLottie } from 'lottie-react';
import notFound from '../../../../assets/not-found-animation.json';
import { StyledDescription } from './ErrorPageContainer.styles';

function ErrorPageContainer() {
  const notFoundAnimation = useLottie({ animationData: notFound }, { width: '40rem' });

  return (
    <>
      <div>{notFoundAnimation.View}</div>
      <StyledDescription>
        찾을 수 없는 페이지입니다.
        <br />
        요청하신 페이지가 사라졌거나, 잘못된 경로로 들어오셨어요 :)
      </StyledDescription>
    </>
  );
}

export default ErrorPageContainer;
