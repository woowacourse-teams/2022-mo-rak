import {
  StyledContainer,
  StyledMenuIcon,
  StyledCloseButton,
  StyledDrawer
} from './NavbarFooter.styles';
import Home from '../../../../assets/home.svg';
import MenuImg from '../../../../assets/menu.svg';
import Poll from '../../../../assets/poll.svg';
import CloseButton from '../../../../assets/close-button.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';
import { useNavigate } from 'react-router-dom';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';
import { Group } from '../../../../types/group';
import { Menu } from '../../../../types/menu';
import Divider from '../../../../components/Divider/Divider';
import NavbarFeaturesSection from '../NavbarFeaturesSection/NavbarFeaturesSection';
import NavbarMembersProfileSection from '../NavbarMembersProfileSection/NavbarMembersProfileSection';
import NavbarGroupsSection from '../NavbarGroupsSection/NavbarGroupsSection';
import NavbarBottomSection from '../NavbarBottomSection/NavbarBottomSection';
import NavbarFooterModals from '../NavbarFooterModals/NavbarFooterModals';

import { useState } from 'react';

type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
};

function NavbarFooter({ groupCode, groups }: Props) {
  const { activeMenu, isDrawerVisible } = useMenuContext();
  const menuDispatch = useMenuDispatchContext();
  const navigate = useNavigate();
  const [activeModalMenu, setActiveModalMenu] = useState<null | string>(null);

  const handleCloseDrawer = () => {
    menuDispatch({ type: 'SET_IS_DRAWER_VISIBLE', payload: false });
  };

  const handleSetActiveDrawer = () => {
    menuDispatch({ type: 'SET_IS_DRAWER_VISIBLE', payload: true });
  };

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleSetActiveMenu = (menu: Menu) => () => {
    menuDispatch({ type: 'SET_ACTIVE_MENU', payload: menu });

    if (menu === 'main') {
      navigate(`/groups/${groupCode}`);
      return;
    }
    navigate(`/groups/${groupCode}/${menu}`);
  };

  const handleSetActiveModalMenu = (menu: null | string) => () => {
    setActiveModalMenu(menu);
    menuDispatch({ type: 'SET_IS_DRAWER_VISIBLE', payload: false });
  };

  return (
    <>
      <StyledContainer>
        <button onClick={handleSetActiveMenu('main')}>
          <StyledMenuIcon src={Home} isActive={activeMenu === 'main'} alt="main-page" />
        </button>
        <button onClick={handleSetActiveDrawer}>
          <StyledMenuIcon src={MenuImg} alt="메뉴 목록" />
        </button>
        <button onClick={handleSetActiveMenu('poll')}>
          <StyledMenuIcon src={Poll} isActive={activeMenu === 'poll'} alt="투표하기 메뉴" />
        </button>
        <button onClick={handleSetActiveMenu('appointment')}>
          <StyledMenuIcon
            src={Appointment}
            isActive={activeMenu === 'appointment'}
            alt="약속잡기 메뉴"
          />
        </button>
        <button onClick={handleSetActiveMenu('role')}>
          <StyledMenuIcon src={Role} isActive={activeMenu === 'role'} alt="역할정하기 메뉴" />
        </button>
      </StyledContainer>

      {/* 메뉴리스트 */}
      <StyledDrawer isVisible={isDrawerVisible}>
        <StyledCloseButton onClick={handleCloseDrawer}>
          <img src={CloseButton} alt="메뉴닫기" />
        </StyledCloseButton>

        <NavbarGroupsSection
          closeDrawer={handleCloseDrawer}
          onClickMenu={handleSetActiveModalMenu}
          groupCode={groupCode}
          groups={groups}
        />

        <Divider />

        <NavbarFeaturesSection closeDrawer={handleCloseDrawer} groupCode={groupCode} />

        <Divider />
        <NavbarMembersProfileSection groupCode={groupCode} />

        <Divider />
        <NavbarBottomSection onClickMenu={handleSetActiveModalMenu} groupCode={groupCode} />
      </StyledDrawer>

      {/* TODO: 모달이 모여있음  */}
      {/* TODO: portal 사용 */}
      {/* TODO: 모달이 기존과 같음 */}
      <NavbarFooterModals
        activeModalMenu={activeModalMenu}
        closeModal={handleSetActiveModalMenu(null)}
        groupCode={groupCode}
      />
    </>
  );
}

export default NavbarFooter;
