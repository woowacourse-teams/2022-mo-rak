import { StyledContainer, StyledMenuIcon } from './MobileMenu.styles';
import Home from '../../../../assets/home.svg';
import MenuImg from '../../../../assets/menu.svg';
import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';
import { useNavigate } from 'react-router-dom';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';
import { GroupInterface } from '../../../../types/group';
import { Menu } from '../../../../types/menu';

interface Props {
  groupCode: GroupInterface['code'];
}

function MobileMenu({ groupCode }: Props) {
  const { activeMenu } = useMenuContext();
  const dispatch = useMenuDispatchContext();
  const navigate = useNavigate();

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleActiveMenu = (menu: Menu) => () => {
    dispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
    if (menu === 'main') {
      navigate(`/groups/${groupCode}`);
      return;
    }
    navigate(`/groups/${groupCode}/${menu}`);
  };

  return (
    <StyledContainer>
      <button onClick={handleActiveMenu('main')}>
        <StyledMenuIcon src={Home} isActive={activeMenu === 'main'} alt="main-page" />
      </button>
      <button>
        <StyledMenuIcon src={MenuImg} alt="메뉴 목록" />
      </button>
      <button onClick={handleActiveMenu('poll')}>
        <StyledMenuIcon src={Poll} isActive={activeMenu === 'poll'} alt="투표하기 메뉴" />
      </button>
      <button onClick={handleActiveMenu('appointment')}>
        <StyledMenuIcon
          src={Appointment}
          isActive={activeMenu === 'appointment'}
          alt="약속잡기 메뉴"
        />
      </button>
      <button onClick={handleActiveMenu('role')}>
        <StyledMenuIcon src={Role} isActive={activeMenu === 'role'} alt="역할정하기 메뉴" />
      </button>
    </StyledContainer>
  );
}

export default MobileMenu;
