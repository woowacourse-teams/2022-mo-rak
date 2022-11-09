import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ theme }) => `
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  position: fixed;
  bottom: 0;
  z-index: 20; // TODO: 상수화
  width: 100%;
  height: 10%;
  background-color:${theme.colors.WHITE_100}; 
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

export { StyledContainer, StyledMenuIcon };
