import { Outlet } from 'react-router-dom';
import { GroupMembersProvider } from '@/context/GroupMembersProvider';
import { NavigationBarProvider } from '@/context/NavigationBarProvider';
import useDeviceState from '@/hooks/useDeviceState';
import GlobalFootbar from '@/layouts/NavigationLayout/components/GlobalFootbar/GlobalFootbar';
import Sidebar from '@/layouts/NavigationLayout/components/Sidebar/Sidebar';
import { StyledContainer } from '@/layouts/NavigationLayout/NavigationLayout.styles';

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
