import CenteredSpinner from '../../components/CenteredSpinner/CenteredSpinner';
import { StyledContainer } from './FallbackPage.styles';

function FallbackPage() {
  return (
    <StyledContainer>
      <CenteredSpinner width="10%" />
    </StyledContainer>
  );
}

export default FallbackPage;
