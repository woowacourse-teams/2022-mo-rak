import { Outlet } from 'react-router-dom';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import { MenuProvider } from '../../context/MenuProvider';
import useDeviceState from '../../hooks/useDeviceState';
import MobileLayout from '../MobileLayout/MobileLayout';
import SidebarLayout from '../SidebarLayout/SidebarLayout';

function Layout() {
  const isMobile = useDeviceState();
  const menuLayout = isMobile ? <MobileLayout /> : <SidebarLayout />;

  return (
    <FlexContainer>
      <MenuProvider>
        {menuLayout}
        <Outlet />
      </MenuProvider>
    </FlexContainer>
  );
}

export default Layout;
