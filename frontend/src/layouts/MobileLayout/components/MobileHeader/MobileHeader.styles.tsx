import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ theme }) => `
  display: flex;
  align-items: center;
  position: fixed;
  top: 0;
  padding-left: 2rem;
  z-index: 1;
  width: 100vw;
  height: 10vh;
  background:${theme.colors.WHITE_100}; 
`
);

const StyledLogo = styled.img`
  width: 8rem;
`;

export { StyledContainer, StyledLogo };
