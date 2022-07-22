import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '../Sidebar/Sidebar';

function SidebarLayout() {
  return (
    <>
      <Sidebar />
      <Outlet />
    </>
  );
}

export default SidebarLayout;
