import { Outlet } from 'react-router-dom';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import Sidebar from './components/Sidebar/Sidebar';
import { MenuProvider } from '../../context/MenuProvider';
import { GroupMembersProvider } from '../../context/GroupMembersProvider';

function SidebarLayout() {
  return (
    <FlexContainer>
      <MenuProvider>
        <GroupMembersProvider>
          <Sidebar />
          <Outlet />
        </GroupMembersProvider>
      </MenuProvider>
    </FlexContainer>
  );
}

export default SidebarLayout;
