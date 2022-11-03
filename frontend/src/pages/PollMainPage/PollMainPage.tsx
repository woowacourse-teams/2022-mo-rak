import { StyledContainer } from '@/pages/PollMainPage/PollMainPage.styles';

import PollMainContainer from '@/pages/PollMainPage/components/PollMainContainer/PollMainContainer';
import PollMainHeader from '@/pages/PollMainPage/components/PollMainHeader/PollMainHeader';

function PollMainPage() {
  return (
    <StyledContainer>
      <PollMainHeader />
      <PollMainContainer />
    </StyledContainer>
  );
}

export default PollMainPage;
