import { StyledContainer } from './PollMainPage.styles';

import PollMainContainer from './components/PollMainContainer/PollMainContainer';
import PollMainHeader from './components/PollMainHeader/PollMainHeader';

function PollMainPage() {
  return (
    <StyledContainer>
      <PollMainHeader />
      <PollMainContainer />
    </StyledContainer>
  );
}

export default PollMainPage;
