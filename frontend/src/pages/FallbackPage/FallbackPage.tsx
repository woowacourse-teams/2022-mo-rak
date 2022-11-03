import Spinner from '@/components/Spinner/Spinner';
import { StyledContainer } from '@/pages/FallbackPage/FallbackPage.styles';

function FallbackPage() {
  return (
    <StyledContainer>
      <Spinner width="10%" placement="center" />
    </StyledContainer>
  );
}

export default FallbackPage;
