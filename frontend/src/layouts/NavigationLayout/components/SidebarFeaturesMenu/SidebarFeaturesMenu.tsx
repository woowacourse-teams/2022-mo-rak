import { useNavigate } from 'react-router-dom';
import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import Divider from '../../../../components/Divider/Divider';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';
import { Menu } from '../../../../types/menu';

import { Group } from '../../../../types/group';
import {
  StyledMenuHeader,
  StyledContainer,
  StyledMenuIcon,
  StyledMenuTitle,
  StyledMenu
} from './SidebarFeaturesMenu.styles';

type Props = {
  groupCode: Group['code'];
};

function SidebarFeaturesMenu({ groupCode }: Props) {
  const { activeMenu } = useMenuContext();
  const dispatch = useMenuDispatchContext();
  const navigate = useNavigate();

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleActiveMenu = (menu: Menu) => () => {
    dispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
    navigate(`/groups/${groupCode}/${menu}`);
  };

  return (
    <StyledContainer>
      <Divider />
      <StyledMenuHeader>기능</StyledMenuHeader>
      <FlexContainer flexDirection="column">
        <StyledMenu onClick={handleActiveMenu('poll')} isActive={activeMenu === 'poll'}>
          <StyledMenuIcon src={Poll} />
          <StyledMenuTitle>투표하기</StyledMenuTitle>
        </StyledMenu>
        <StyledMenu
          onClick={handleActiveMenu('appointment')}
          isActive={activeMenu === 'appointment'}
        >
          <StyledMenuIcon src={Appointment} />
          <StyledMenuTitle>약속잡기</StyledMenuTitle>
        </StyledMenu>
        <StyledMenu onClick={handleActiveMenu('role')} isActive={activeMenu === 'role'}>
          <StyledMenuIcon src={Role} />
          <StyledMenuTitle>역할 정하기</StyledMenuTitle>
        </StyledMenu>
      </FlexContainer>
    </StyledContainer>
  );
}

export default SidebarFeaturesMenu;
