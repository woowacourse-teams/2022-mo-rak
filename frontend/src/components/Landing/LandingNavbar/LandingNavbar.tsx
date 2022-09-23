import styled from '@emotion/styled';

function LandingNavbar() {
  return (
    <StyledNavbar>
      <StyledMenu href="#main-section">LOGIN</StyledMenu>
      <StyledMenu href="#service-introduction-section">ABOUT</StyledMenu>
      <StyledMenu href="#feature-introduction-section">FEATURES</StyledMenu>
  </StyledNavbar>
  );
}

const StyledNavbar = styled.nav`
  position: absolute;
  right: 12rem;
  top: 6rem;
  display: flex;
  list-style: none;
  gap: 4rem;
  font-size: 2rem;
  cursor: pointer;
`;

const StyledMenu = styled.a(
  ({ theme }) => `
  text-decoration: none;
  color: ${theme.colors.BLACK_100};
`
);

export default LandingNavbar;