import { Outlet } from 'react-router-dom';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import Sidebar from './components/Sidebar/Sidebar';
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
