import { StyledContainer } from './ErrorPage.styles';
import ErrorPageButtons from './components/ErrorPageButtons/ErrorPageButtons';
import ErrorPageContainer from './components/ErrorPageContainer/ErrorPageContainer';

function ErrorPage() {
  return (
    <StyledContainer>
      <ErrorPageContainer />
      <ErrorPageButtons />
    </StyledContainer>
  );
}

export default ErrorPage;
