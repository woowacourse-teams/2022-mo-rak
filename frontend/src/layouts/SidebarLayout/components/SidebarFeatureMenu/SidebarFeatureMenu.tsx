import { useNavigate } from 'react-router-dom';
import Poll from '../../../../assets/person-check.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';

import { GroupInterface } from '../../../../types/group';
import {
  StyledMenuHeader,
  StyledFeatureContainer,
  StyledPollIcon,
  StyledAppointmentIcon,
  StyledFeatureTitle,
  StyledPollMenu,
  StyledAppointmentMenu
} from './SidebarFeatureMenu.styles';

interface Props {
  groupCode: GroupInterface['code'];
}

function SidebarFeatureMenu({ groupCode }: Props) {
  const { activeMenu } = useMenuContext();
  const dispatch = useMenuDispatchContext();
  const navigate = useNavigate();

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleActiveMenu = (menu: 'poll' | 'appointment') => () => {
    dispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
    navigate(`/groups/${groupCode}/${menu}`);
  };

  return (
    <StyledFeatureContainer>
      <StyledMenuHeader>기능</StyledMenuHeader>
      <FlexContainer flexDirection="column">
        <StyledPollMenu onClick={handleActiveMenu('poll')} isActive={activeMenu === 'poll'}>
          <StyledPollIcon src={Poll} />
          <StyledFeatureTitle>투표하기</StyledFeatureTitle>
        </StyledPollMenu>
        <StyledAppointmentMenu
          onClick={handleActiveMenu('appointment')}
          isActive={activeMenu === 'appointment'}
        >
          <StyledAppointmentIcon src={Appointment} />
          <StyledFeatureTitle>약속잡기</StyledFeatureTitle>
        </StyledAppointmentMenu>
      </FlexContainer>
    </StyledFeatureContainer>
  );
}

export default SidebarFeatureMenu;
