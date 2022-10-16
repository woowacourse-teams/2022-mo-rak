import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ theme }) => `
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  position: fixed;
  bottom: 0;
  z-index: 1;
  width: 100vw;
  height: 10vh;
  background:${theme.colors.WHITE_100}; 
`
);

const StyledMenuIcon = styled.img`
  //TODO: filter도 theme으로 변경해줄까?

  filter: invert(59%) sepia(6%) saturate(17%) hue-rotate(314deg) brightness(103%) contrast(77%);
  width: 5.2rem;

  &:hover {
    filter: invert(32%) sepia(66%) saturate(3979%) hue-rotate(241deg) brightness(93%) contrast(91%);
  }
`;

export { StyledContainer, StyledMenuIcon };
