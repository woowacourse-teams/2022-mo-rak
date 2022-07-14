import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '../common/Sidebar/Sidebar';

function SidebarLayout() {
  return (
    <>
      <Sidebar />
      <Outlet />
    </>
  );
}

export default SidebarLayout;
