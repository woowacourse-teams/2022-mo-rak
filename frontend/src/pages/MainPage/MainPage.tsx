import styled from '@emotion/styled';

import MainFeatureMenuContainer from '../../components/Main/MainFeatureMenuContainer/MainFeatureMenuContainer';

function MainPage() {
  return (
    <StyledContainer>
      <MainFeatureMenuContainer />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  padding: 6.4rem 20rem;
`;

export default MainPage;
