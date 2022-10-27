import { useNavigate } from 'react-router-dom';
import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import useNavigationBarDispatchContext from '../../../../hooks/useNavigationBarDispatchContext';
import useNavigationBarContext from '../../../../hooks/useNavigationBarContext';
import { Menu } from '../../../../types/menu';

import { Group } from '../../../../types/group';
import {
  StyledMenuHeader,
  StyledContainer,
  StyledMenuIcon,
  StyledMenuTitle,
  StyledMenu
} from './GlobalFootbarFootbarDrawerFeaturesSection.styles';

type Props = {
  groupCode: Group['code'];
  closeDrawer: () => void;
};

function GlobalFootbarFootbarDrawerFeaturesSection({ closeDrawer, groupCode }: Props) {
  const { activeMenu } = useNavigationBarContext();
  const navigationBarDispatch = useNavigationBarDispatchContext();
  const navigate = useNavigate();

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleActiveMenu = (menu: Menu) => () => {
    navigationBarDispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
    navigate(`/groups/${groupCode}/${menu}`);
    closeDrawer();
  };

  return (
    <StyledContainer>
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

export default GlobalFootbarFootbarDrawerFeaturesSection;
