import React from 'react';
import { Outlet } from 'react-router-dom';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import Sidebar from '../Sidebar/Sidebar';
import { MenuProvider } from '../../context/MenuProvider';

function SidebarLayout() {
  return (
    <FlexContainer>
      <MenuProvider>
        <Sidebar />
        <Outlet />
      </MenuProvider>
    </FlexContainer>
  );
}

export default SidebarLayout;
