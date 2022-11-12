import { StyledContainer } from '@/pages/FallbackPage/FallbackPage.styles';

import Spinner from '@/components/Spinner/Spinner';

function FallbackPage() {
  return (
    <StyledContainer>
      <Spinner width="10%" placement="center" />
    </StyledContainer>
  );
}

export default FallbackPage;
