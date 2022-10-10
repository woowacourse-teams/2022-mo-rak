import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ theme }) => `
  position: sticky;
  top: 0;
  width: 36.4rem;
  height: 100vh;
  z-index: 1; 
  background: ${theme.colors.WHITE_100};
  padding-left: 4rem;
  gap: 2rem;
`
);

const StyledLogo = styled.img`
  display: block;
  margin: 2rem auto;
  width: 16rem;
  aspect-ratio: 16 / 9;
  cursor: pointer;
  padding-right: 4rem;
`;

const StyledBottomMenu = styled.div`
  position: absolute;
  bottom: 4rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

export { StyledContainer, StyledLogo, StyledBottomMenu };
