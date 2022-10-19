import { StyledContainer } from './ErrorPage.styles';
import ErrorPageButtonGroup from './components/ErrorPageButtonGroup/ErrorPageButtonGroup';
import ErrorPageContainer from './components/ErrorPageContainer/ErrorPageContainer';

function ErrorPage() {
  return (
    <StyledContainer>
      <ErrorPageContainer />
      <ErrorPageButtonGroup />
    </StyledContainer>
  );
}

export default ErrorPage;
