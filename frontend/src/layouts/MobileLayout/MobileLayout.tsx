import { Outlet } from 'react-router-dom';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import { MenuProvider } from '../../context/MenuProvider';
import MobileContainer from './components/MobileContainer/MobileContainer';

function MobileLayout() {
  return (
    <FlexContainer>
      <MenuProvider>
        <MobileContainer />
        <Outlet />
      </MenuProvider>
    </FlexContainer>
  );
}

export default MobileLayout;
