import { StyledContainer } from './MainPage.styles';

import MainFeatureMenuContainer from './components/MainFeatureMenuContainer/MainFeatureMenuContainer';
import MainFeatureMenuHeader from './components/MainFeatureMenuHeader/MainFeatureMenuHeader';

function MainPage() {
  return (
    <StyledContainer>
      <MainFeatureMenuHeader />
      <MainFeatureMenuContainer />
    </StyledContainer>
  );
}

export default MainPage;
