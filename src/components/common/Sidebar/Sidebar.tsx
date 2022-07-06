import React from 'react';
import styled from '@emotion/styled';

function Sidebar() {
  return <Container>SIDEBAR🛠</Container>;
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

export default Sidebar;
