import { Outlet } from 'react-router-dom';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import { MenuProvider } from '../../context/MenuProvider';
import MobileHeader from './components/MobileHeader/MobileHeader';
import MobileMenu from './components/MobileMenu/MobileMenu';

function MobileLayout() {
  return (
    <FlexContainer>
      <MenuProvider>
        <MobileHeader />
        <MobileMenu />
        <Outlet />
      </MenuProvider>
    </FlexContainer>
  );
}

export default MobileLayout;
