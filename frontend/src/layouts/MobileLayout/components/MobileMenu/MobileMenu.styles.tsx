import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ theme }) => `
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  position: fixed;
  bottom: 0;
  z-index: 1;
  width: 100%;
  height: 10%;
  background:${theme.colors.WHITE_100}; 
  border-radius: 10px 10px 0 0;
`
);

const StyledMenuIcon = styled.img<{
  isActive?: boolean;
}>(
  ({ isActive }) => `
  width: 5.2rem;
  &:hover {
    filter: invert(32%) sepia(66%) saturate(3979%) hue-rotate(241deg) brightness(93%) contrast(91%);
  }
  
  //TODO: filter도 theme으로 변경해줄까?
  filter: ${
    isActive
      ? `invert(45%) sepia(82%) saturate(6262%) hue-rotate(243deg) brightness(94%) contrast(88%)`
      : `invert(59%) sepia(6%) saturate(17%) hue-rotate(314deg) brightness(103%) contrast(77%)`
  }
`
);

const StyledMenuListContainer = styled.div<{ isVisible: boolean }>(
  ({ isVisible, theme }) => `
  display: ${isVisible ? 'flex' : 'none'};
  flex-direction: column;
  position: absolute;
  top: 0;
  width: 100%;
  min-height: 100%;
  z-index: 1; 
  background: ${theme.colors.WHITE_100};
  padding: 4rem;
  gap: 2rem;
`
);

const StyledCloseButton = styled.button`
  position: absolute;
  top: 4rem;
  right: 4rem;
  z-index: 1;
`;

const StyledBottomMenu = styled.div`
  margin-top: 2.8rem;
  display: flex;
  flex-direction: column;
  gap: 4rem;
`;

export {
  StyledContainer,
  StyledMenuIcon,
  StyledMenuListContainer,
  StyledCloseButton,
  StyledBottomMenu
};
