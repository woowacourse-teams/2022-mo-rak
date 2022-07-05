import React from 'react';
import styled from '@emotion/styled';

function Sidebar() {
  return <Container>thisissidebar</Container>;
}

const Container = styled.div(
  ({ theme }) => `
  width: 364px;
  height: 100vh;
  position: sticky;
  top: 0;
  border-right: 1px solid ${theme.colors.GRAY_200};
  background: ${theme.colors.WHITE_100};
`
);

export default Sidebar;
