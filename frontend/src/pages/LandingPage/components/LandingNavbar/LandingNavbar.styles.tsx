import styled from '@emotion/styled';

const StyledNavbar = styled.nav`
  position: absolute;
  right: 12rem;
  top: 6rem;
  display: flex;
  list-style: none;
  gap: 4rem;
  font-size: 2rem;
  cursor: pointer;

  @media (max-width: 700px) {
    display: none;
  }
`;

const StyledMenu = styled.a(
  ({ theme }) => `
  text-decoration: none;
  color: ${theme.colors.BLACK_100};
`
);

export { StyledMenu, StyledNavbar };
