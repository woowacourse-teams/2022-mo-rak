import { StyledContainer, StyledMenuIcon } from './NavbarFooter.styles';
import Home from '../../../../assets/home.svg';
import MenuImg from '../../../../assets/menu.svg';
import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';
import { useNavigate } from 'react-router-dom';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';
import { Group } from '../../../../types/group';
import { Menu } from '../../../../types/menu';
import NavbarDrawer from '../NavbarDrawer/NavbarDrawer';

type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
};

function NavbarFooter({ groupCode, groups }: Props) {
  const { activeMenu } = useMenuContext();
  const menuDispatch = useMenuDispatchContext();
  const navigate = useNavigate();

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

      <NavbarDrawer groupCode={groupCode} groups={groups} />
    </>
  );
}

export default NavbarFooter;
