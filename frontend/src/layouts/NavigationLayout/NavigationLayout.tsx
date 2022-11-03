import { Outlet } from 'react-router-dom';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import { GroupMembersProvider } from '@/context/GroupMembersProvider';
import { NavigationBarProvider } from '@/context/NavigationBarProvider';
import useDeviceState from '@/hooks/useDeviceState';
import GlobalFootbar from '@/layouts/NavigationLayout/components/GlobalFootbar/GlobalFootbar';
import Sidebar from '@/layouts/NavigationLayout/components/Sidebar/Sidebar';

function NavigationLayout() {
  const isMobile = useDeviceState();
  const navigationBar = isMobile ? <GlobalFootbar /> : <Sidebar />;

  return (
    <FlexContainer>
      <NavigationBarProvider>
        <GroupMembersProvider>
          {navigationBar}
          <Outlet />
        </GroupMembersProvider>
      </NavigationBarProvider>
    </FlexContainer>
  );
}

export default NavigationLayout;
