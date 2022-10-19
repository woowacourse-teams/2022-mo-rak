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
`
);

const StyledLogoContainer = styled.div`
  height: 10%;
`;

const StyledLogo = styled.img`
  display: block;
  margin: 0 auto;
  height: 100%;
  max-height: 16rem;
  aspect-ratio: 16 / 9;
  cursor: pointer;
  padding: 2rem 4rem 2rem 0;
`;

const StyledBottomMenu = styled.div`
  height: 15%;
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

export { StyledContainer, StyledLogo, StyledBottomMenu, StyledLogoContainer };
