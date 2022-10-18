import {
  StyledContainer,
  StyledMenuIcon,
  StyledMenusContainer,
  StyledCloseButton,
  StyledBottomMenu
} from './NavbarMenu.styles';
import Home from '../../../../assets/home.svg';
import MenuImg from '../../../../assets/menu.svg';
import Poll from '../../../../assets/poll.svg';
import CloseButton from '../../../../assets/close-button.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';
import { useNavigate } from 'react-router-dom';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';
import { GroupInterface } from '../../../../types/group';
import { Menu } from '../../../../types/menu';
import Divider from '../../../../components/Divider/Divider';
import NavbarFeaturesMenu from '../NavbarFeaturesMenu/NavbarFeaturesMenu';
import NavbarMembersProfileMenu from '../NavbarMembersProfileMenu/NavbarMembersProfileMenu';
import NavbarSlackMenu from '../NavbarSlackMenu/NavbarSlackMenu';
import NavbarInvitationMenu from '../NavbarInvitationMenu/NavbarInvitationMenu';
import NavbarLogoutMenu from '../NavbarLogoutMenu/NavbarLogoutMenu';
import NavbarGroupsMenu from '../NavbarGroupsMenu/NavbarGroupsMenu';
import NavbarMenuModals from '../NavbarMenuModals/NavbarMenuModals';

import { useState } from 'react';

interface Props {
  groupCode: GroupInterface['code'];
  groups: Array<GroupInterface>;
}

function NavbarMenu({ groupCode, groups }: Props) {
  const { activeMenu, isVisibleMenus } = useMenuContext();
  const dispatch = useMenuDispatchContext();
  const navigate = useNavigate();
  const [activeModalMenu, setActiveModalMenu] = useState<null | string>(null);

  const handleCloseMenus = () => {
    dispatch({ type: 'SET_IS_VISIBLE_MENUS', payload: false });
  };

  const handleSetActiveMenus = () => {
    dispatch({ type: 'SET_IS_VISIBLE_MENUS', payload: true });
  };

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleSetActiveMenu = (menu: Menu) => () => {
    dispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
    if (menu === 'main') {
      navigate(`/groups/${groupCode}`);
      return;
    }
    navigate(`/groups/${groupCode}/${menu}`);
  };

  const handleSetActiveModalMenu = (menu: null | string) => () => {
    setActiveModalMenu(menu);
    dispatch({ type: 'SET_IS_VISIBLE_MENUS', payload: false });
  };

  return (
    <>
      <StyledContainer>
        <button onClick={handleSetActiveMenu('main')}>
          <StyledMenuIcon src={Home} isActive={activeMenu === 'main'} alt="main-page" />
        </button>
        <button onClick={handleSetActiveMenus}>
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
      <StyledMenusContainer isVisible={isVisibleMenus}>
        <StyledCloseButton onClick={handleCloseMenus}>
          <img src={CloseButton} alt="메뉴닫기" />
        </StyledCloseButton>
        <NavbarGroupsMenu
          onClickCloseMenusMenu={handleCloseMenus}
          onClickMenu={handleSetActiveModalMenu}
          groupCode={groupCode}
          groups={groups}
        />

        <Divider />

        <NavbarFeaturesMenu onClickMenu={handleCloseMenus} groupCode={groupCode} />

        <Divider />
        <NavbarMembersProfileMenu groupCode={groupCode} />

        <Divider />
        <StyledBottomMenu>
          <NavbarSlackMenu onClick={handleSetActiveModalMenu('slack')} />
          <NavbarInvitationMenu groupCode={groupCode} />
          <NavbarLogoutMenu />
        </StyledBottomMenu>
      </StyledMenusContainer>

      {/* TODO: 모달이 모여있음  */}
      {/* TODO: portal 사용 */}
      {/* TODO: 모달이 기존과 같음 */}
      <NavbarMenuModals
        activeModalMenu={activeModalMenu}
        closeModal={handleSetActiveModalMenu(null)}
        groupCode={groupCode}
      />
    </>
  );
}

export default NavbarMenu;
