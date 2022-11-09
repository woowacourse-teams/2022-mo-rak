import { StyledContainer } from '@/pages/ErrorPage/ErrorPage.styles';
import ErrorPageButtons from '@/pages/ErrorPage/components/ErrorPageButtons/ErrorPageButtons';
import ErrorPageContainer from '@/pages/ErrorPage/components/ErrorPageContainer/ErrorPageContainer';

function ErrorPage() {
  return (
    <StyledContainer>
      <ErrorPageContainer />
      <ErrorPageButtons />
    </StyledContainer>
  );
}

export default ErrorPage;
