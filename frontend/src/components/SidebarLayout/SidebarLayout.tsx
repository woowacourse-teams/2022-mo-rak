import React from 'react';
import { Outlet } from 'react-router-dom';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import Sidebar from '../Sidebar/Sidebar';

function SidebarLayout() {
  return (
    <FlexContainer>
      <Sidebar />
      <Outlet />
    </FlexContainer>
  );
}

export default SidebarLayout;
