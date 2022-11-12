import { useNavigate } from 'react-router-dom';

import {
  StyledContainer,
  StyledMenu,
  StyledMenuHeader,
  StyledMenuIcon,
  StyledMenuTitle
} from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerFeaturesSection/GlobalFootbarFootbarDrawerFeaturesSection.styles';

import FlexContainer from '@/components/FlexContainer/FlexContainer';

import useNavigationBarContext from '@/hooks/useNavigationBarContext';
import useNavigationBarDispatchContext from '@/hooks/useNavigationBarDispatchContext';

import calendarClockImg from '@/assets/calendar-clock.svg';
import pollImg from '@/assets/poll.svg';
import roleImg from '@/assets/role.svg';
import { Group } from '@/types/group';
import { Menu } from '@/types/menu';

type Props = {
  groupCode: Group['code'];
  closeDrawer: () => void;
};

function GlobalFootbarFootbarDrawerFeaturesSection({ closeDrawer, groupCode }: Props) {
  const { activeMenu } = useNavigationBarContext();
  const navigationBarDispatch = useNavigationBarDispatchContext();
  const navigate = useNavigate();

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

export default GlobalFootbarFootbarDrawerFeaturesSection;
