import { useNavigate } from 'react-router-dom';
import pollImg from '../../../../assets/poll.svg';
import calendarClockImg from '../../../../assets/calendar-clock.svg';
import roleImg from '../../../../assets/role.svg';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';
import { Menu } from '../../../../types/menu';

import { Group } from '../../../../types/group';
import {
  StyledContainer,
  StyledMenuHeader,
  StyledMenuIcon,
  StyledMenuTitle,
  StyledMenu
} from './SidebarFeaturesMenu.styles';

type Props = {
  groupCode: Group['code'];
};

function SidebarFeaturesMenu({ groupCode }: Props) {
  const { activeMenu } = useMenuContext();
  const menuDispatch = useMenuDispatchContext();
  const navigate = useNavigate();

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleActiveMenu = (menu: Menu) => () => {
    menuDispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
    navigate(`/groups/${groupCode}/${menu}`);
  };

  return (
    <StyledContainer>
      <StyledMenuHeader>기능</StyledMenuHeader>
      <FlexContainer flexDirection="column">
        <StyledMenu onClick={handleActiveMenu('poll')} isActive={activeMenu === 'poll'}>
          <StyledMenuIcon src={pollImg} />
          <StyledMenuTitle>투표하기</StyledMenuTitle>
        </StyledMenu>
        <StyledMenu
          onClick={handleActiveMenu('appointment')}
          isActive={activeMenu === 'appointment'}
        >
          <StyledMenuIcon src={calendarClockImg} />
          <StyledMenuTitle>약속잡기</StyledMenuTitle>
        </StyledMenu>
        <StyledMenu onClick={handleActiveMenu('role')} isActive={activeMenu === 'role'}>
          <StyledMenuIcon src={roleImg} />
          <StyledMenuTitle>역할 정하기</StyledMenuTitle>
        </StyledMenu>
      </FlexContainer>
    </StyledContainer>
  );
}

export default SidebarFeaturesMenu;
