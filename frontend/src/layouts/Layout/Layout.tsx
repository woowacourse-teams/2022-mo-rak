import { Outlet } from 'react-router-dom';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import { GroupMembersProvider } from '../../context/GroupMembersProvider';
import { MenuProvider } from '../../context/MenuProvider';
import useDeviceState from '../../hooks/useDeviceState';
import NavbarLayout from '../NavbarLayout/NavbarLayout';
import SidebarLayout from '../SidebarLayout/SidebarLayout';

function Layout() {
  const isMobile = useDeviceState();
  const menuLayout = isMobile ? <NavbarLayout /> : <SidebarLayout />;

  return (
    <FlexContainer>
      <MenuProvider>
        <GroupMembersProvider>
          {menuLayout}
          <Outlet />
        </GroupMembersProvider>
      </MenuProvider>
    </FlexContainer>
  );
}

export default Layout;
