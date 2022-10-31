import { Outlet } from 'react-router-dom';
import { GroupMembersProvider } from '../../context/GroupMembersProvider';
import { NavigationBarProvider } from '../../context/NavigationBarProvider';
import useDeviceState from '../../hooks/useDeviceState';
import GlobalFootbar from './components/GlobalFootbar/GlobalFootbar';
import { StyledContainer } from './NavigationLayout.styles';
import Sidebar from './components/Sidebar/Sidebar';

function NavigationLayout() {
  const isMobile = useDeviceState();
  const navigationBar = isMobile ? <GlobalFootbar /> : <Sidebar />;

  return (
    <StyledContainer>
      <NavigationBarProvider>
        <GroupMembersProvider>
          {navigationBar}
          <Outlet />
        </GroupMembersProvider>
      </NavigationBarProvider>
    </StyledContainer>
  );
}

export default NavigationLayout;
