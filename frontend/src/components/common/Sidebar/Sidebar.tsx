import React from 'react';
import styled from '@emotion/styled';
import Logo from '../../../assets/logo.svg';
import FlexContainer from '../FlexContainer/FlexContainer';

function Sidebar() {
  return (
    <Container>
      <FlexContainer alignItems="center" justifyContent="center">
        <LogoStyled src={Logo} alt={Logo} />
      </FlexContainer>
    </Container>
  );
}

const Container = styled.div(
  ({ theme }) => `
  width: 36.4rem;
  height: 100vh;
  position: sticky;
  top: 0;
  border-right: 0.1rem solid ${theme.colors.GRAY_200};
  background: ${theme.colors.WHITE_100};
`
);

const LogoStyled = styled.img`
  width: 200px;
  height: 100%;
`;

export default Sidebar;
