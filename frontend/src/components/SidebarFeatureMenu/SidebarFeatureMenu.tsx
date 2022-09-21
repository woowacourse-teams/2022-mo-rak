import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';
import Poll from '../../assets/person-check.svg';
import Appointment from '../../assets/calendar-clock.svg';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import useMenuDispatchContext from '../../hooks/useMenuDispatchContext';
import useMenuContext from '../../hooks/useMenuContext';

import { GroupInterface } from '../../types/group';

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

const StyledMenuHeader = styled.div`
  font-size: 1.7rem; // TODO: 4단위로 변경
  text-align: left;
  margin-bottom: 2rem;
`;

const StyledFeatureContainer = styled.div`
  margin: 2.8rem 0;
`;

const StyledPollIcon = styled.img`
  width: 2.4rem;
`;

const StyledAppointmentIcon = styled.img`
  width: 2.4rem;
`;

const StyledFeatureTitle = styled.div`
  font-size: 1.6rem;
`;

const StyledPollMenu = styled.div<{
  isActive: boolean;
}>(
  ({ isActive, theme }) => `
  display: flex;
  gap: 2rem;
  cursor: pointer;
  padding: 2rem;

  ${
    isActive &&
    `
    background: ${theme.colors.GRAY_100}; 
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;`
  };

  &:hover {
    background: ${!isActive && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

const StyledAppointmentMenu = styled.div<{
  isActive: boolean;
}>(
  ({ isActive, theme }) => `
  display: flex;
  gap: 2rem;
  cursor: pointer;
  padding: 2rem;
  
  ${
    isActive &&
    `
  background: ${theme.colors.GRAY_100}; 
  border-top-left-radius: 4rem; 
  border-bottom-left-radius: 4rem;
  `
  };

  &:hover {
    background: ${!isActive && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

export default SidebarFeatureMenu;
