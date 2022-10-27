import { Outlet } from 'react-router-dom';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import { GroupMembersProvider } from '../../context/GroupMembersProvider';
import { MenuProvider } from '../../context/MenuProvider';
import useDeviceState from '../../hooks/useDeviceState';
import GlobalFootbar from './components/GlobalFootbar/GlobalFootbar';
import Sidebar from './components/Sidebar/Sidebar';

function NavigationLayout() {
  const isMobile = useDeviceState();
  const navigationBar = isMobile ? <GlobalFootbar /> : <Sidebar />;

  return (
    <FlexContainer>
      <MenuProvider>
        <GroupMembersProvider>
          {navigationBar}
          <Outlet />
        </GroupMembersProvider>
      </MenuProvider>
    </FlexContainer>
  );
}

export default NavigationLayout;
