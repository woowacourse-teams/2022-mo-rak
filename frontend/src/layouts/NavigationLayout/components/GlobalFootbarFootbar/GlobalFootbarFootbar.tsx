import { StyledContainer, StyledMenuIcon } from './GlobalFootbarFootbar.styles';
import Home from '../../../../assets/home.svg';
import MenuImg from '../../../../assets/menu.svg';
import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';
import { useNavigate } from 'react-router-dom';
import useNavigationBarDispatchContext from '../../../../hooks/useNavigationBarDispatchContext';
import useNavigationBarContext from '../../../../hooks/useNavigationBarContext';
import { Group } from '../../../../types/group';
import { Menu } from '../../../../types/menu';
import GlobalFootbarFootbarDrawer from '../GlobalFootbarFootbarDrawer/GlobalFootbarFootbarDrawer';

type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
};

function GlobalFootbarFootbar({ groupCode, groups }: Props) {
  const { activeMenu } = useNavigationBarContext();
  const navigationBarDispatch = useNavigationBarDispatchContext();
  const navigate = useNavigate();

  const handleSetActiveDrawer = () => {
    navigationBarDispatch({ type: 'SET_IS_DRAWER_VISIBLE', payload: true });
  };

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleSetActiveMenu = (menu: Menu) => () => {
    navigationBarDispatch({ type: 'SET_ACTIVE_MENU', payload: menu });

    if (menu === null) {
      navigate(`/groups/${groupCode}`);
      return;
    }
    navigate(`/groups/${groupCode}/${menu}`);
  };

  return (
    <>
      <StyledContainer>
        <button onClick={handleSetActiveMenu(null)}>
          <StyledMenuIcon src={Home} isActive={activeMenu === null} alt="main-page" />
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

      <GlobalFootbarFootbarDrawer groupCode={groupCode} groups={groups} />
    </>
  );
}

export default GlobalFootbarFootbar;
