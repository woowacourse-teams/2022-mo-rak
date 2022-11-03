import { StyledContainer, StyledMenuIcon } from './GlobalFootbarFootbar.styles';
import homeImg from '@/assets/home.svg';
import menuImg from '@/assets/menu.svg';
import pollImg from '@/assets/poll.svg';
import calendarClockImg from '@/assets/calendar-clock.svg';
import roleImg from '@/assets/role.svg';
import { useNavigate } from 'react-router-dom';
import useNavigationBarDispatchContext from '@/hooks/useNavigationBarDispatchContext';
import useNavigationBarContext from '@/hooks/useNavigationBarContext';
import { Group } from '@/types/group';
import { Menu } from '@/types/menu';
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
          <StyledMenuIcon src={homeImg} isActive={activeMenu === null} alt="main-page" />
        </button>
        <button onClick={handleSetActiveDrawer}>
          <StyledMenuIcon src={menuImg} alt="메뉴 목록" />
        </button>
        <button onClick={handleSetActiveMenu('poll')}>
          <StyledMenuIcon src={pollImg} isActive={activeMenu === 'poll'} alt="투표하기 메뉴" />
        </button>
        <button onClick={handleSetActiveMenu('appointment')}>
          <StyledMenuIcon
            src={calendarClockImg}
            isActive={activeMenu === 'appointment'}
            alt="약속잡기 메뉴"
          />
        </button>
        <button onClick={handleSetActiveMenu('role')}>
          <StyledMenuIcon src={roleImg} isActive={activeMenu === 'role'} alt="역할정하기 메뉴" />
        </button>
      </StyledContainer>

      <GlobalFootbarFootbarDrawer groupCode={groupCode} groups={groups} />
    </>
  );
}

export default GlobalFootbarFootbar;
