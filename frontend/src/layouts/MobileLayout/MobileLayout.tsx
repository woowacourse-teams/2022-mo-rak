import { Outlet, useParams } from 'react-router-dom';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import { MenuProvider } from '../../context/MenuProvider';
import MobileHeader from './components/MobileHeader/MobileHeader';
import MobileMenu from './components/MobileMenu/MobileMenu';
import { GroupInterface } from '../../types/group';

function MobileLayout() {
  const { groupCode } = useParams() as {
    groupCode: GroupInterface['code'];
  };
  return (
    <FlexContainer>
      <MenuProvider>
        <MobileHeader groupCode={groupCode} />
        <MobileMenu groupCode={groupCode} />
        <Outlet />
      </MenuProvider>
    </FlexContainer>
  );
}

export default MobileLayout;
