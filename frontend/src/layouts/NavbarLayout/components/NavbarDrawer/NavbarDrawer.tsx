import { useState } from 'react';

import { StyledContainer, StyledCloseButton } from './NavbarDrawer.styles';
import CloseButton from '../../../../assets/close-button.svg';
import Divider from '../../../../components/Divider/Divider';
import NavbarDrawerFeaturesSection from '../NavbarDrawerFeaturesSection/NavbarDrawerFeaturesSection';
import NavbarDrawerMembersProfileSection from '../NavbarDrawerMembersProfileSection/NavbarDrawerMembersProfileSection';
import NavbarDrawerGroupsSection from '../NavbarDrawerGroupsSection/NavbarDrawerGroupsSection';
import NavbarDrawerBottomSection from '../NavbarDrawerBottomSection/NavbarDrawerBottomSection';
import NavbarDrawerModals from '../NavbarDrawerModals/NavbarDrawerModals';
import useMenuContext from '../../../../hooks/useMenuContext';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import { Group } from '../../../../types/group';

// TODO: props drilling 발생 (groups)
type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
};

function NavbarDrawer({ groupCode, groups }: Props) {
  const { isDrawerVisible } = useMenuContext();
  const menuDispatch = useMenuDispatchContext();
  const [activeModalMenu, setActiveModalMenu] = useState<null | string>(null);

  const handleCloseDrawer = () => {
    menuDispatch({ type: 'SET_IS_DRAWER_VISIBLE', payload: false });
  };

  const handleSetActiveModalMenu = (menu: null | string) => () => {
    setActiveModalMenu(menu);
    menuDispatch({ type: 'SET_IS_DRAWER_VISIBLE', payload: false });
  };

  return (
    <>
      <StyledContainer isVisible={isDrawerVisible}>
        <StyledCloseButton onClick={handleCloseDrawer}>
          <img src={CloseButton} alt="메뉴닫기" />
        </StyledCloseButton>

        <NavbarDrawerGroupsSection
          closeDrawer={handleCloseDrawer}
          onClickMenu={handleSetActiveModalMenu}
          groupCode={groupCode}
          groups={groups}
        />

        <Divider />
        <NavbarDrawerFeaturesSection closeDrawer={handleCloseDrawer} groupCode={groupCode} />

        <Divider />
        <NavbarDrawerMembersProfileSection groupCode={groupCode} />

        <Divider />
        <NavbarDrawerBottomSection onClickMenu={handleSetActiveModalMenu} groupCode={groupCode} />
      </StyledContainer>

      {/* TODO: 모달이 모여있음  */}
      {/* TODO: portal 사용 */}
      {/* TODO: 모달이 기존과 같음 */}
      <NavbarDrawerModals
        activeModalMenu={activeModalMenu}
        closeModal={handleSetActiveModalMenu(null)}
        groupCode={groupCode}
      />
    </>
  );
}

export default NavbarDrawer;
