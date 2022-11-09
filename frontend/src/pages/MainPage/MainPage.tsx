import { StyledContainer } from '@/pages/MainPage/MainPage.styles';

import MainFeatureMenuContainer from '@/pages/MainPage/components/MainFeatureMenuContainer/MainFeatureMenuContainer';
import MainFeatureMenuHeader from '@/pages/MainPage/components/MainFeatureMenuHeader/MainFeatureMenuHeader';

function MainPage() {
  return (
    <StyledContainer>
      <MainFeatureMenuHeader />
      <MainFeatureMenuContainer />
    </StyledContainer>
  );
}

export default MainPage;
