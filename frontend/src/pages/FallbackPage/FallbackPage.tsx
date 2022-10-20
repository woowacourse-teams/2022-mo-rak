import Spinner from '../../components/Spinner/Spinner';
import { StyledContainer } from './FallbackPage.styles';

function FallbackPage() {
  return (
    <StyledContainer>
      <Spinner width="10%" placement="center" />
    </StyledContainer>
  );
}

export default FallbackPage;
