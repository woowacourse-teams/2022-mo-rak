import React from 'react';
import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';
import Poll from '../../assets/person-check.svg';
import Appointment from '../../assets/calendar-clock.svg';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import { useMenuDispatchContext, useMenuContext } from '../../context/MenuProvider';
import { GroupInterface } from '../../types/group';

interface Props {
  groupCode: GroupInterface['code'];
}

function SidebarFeatureMenu({ groupCode }: Props) {
  const { clickedMenu } = useMenuContext();
  const dispatch = useMenuDispatchContext();
  const navigate = useNavigate();

  // TODO: 함수 역할에 맞게 분리
  const handleMoveToClickedMenu = (menu: string) => () => {
    dispatch({ type: 'SET_CLICKED_MENU', payload: menu });
    navigate(`/groups/${groupCode}/${menu}`);
  };

  return (
    <StyledFeatureContainer>
      <StyledMenuHeader>기능</StyledMenuHeader>
      <FlexContainer flexDirection="column">
        <StyledPollMenu
          onClick={handleMoveToClickedMenu('poll')}
          isClicked={clickedMenu === 'poll'}
        >
          <StyledPollIcon src={Poll} />
          <StyledFeatureTitle>투표하기</StyledFeatureTitle>
        </StyledPollMenu>
        <StyledAppointmentMenu
          onClick={handleMoveToClickedMenu('appointment')}
          isClicked={clickedMenu === 'appointment'}
        >
          <StyledAppointmentIcon src={Appointment} />
          <StyledFeatureTitle>약속잡기</StyledFeatureTitle>
        </StyledAppointmentMenu>
      </FlexContainer>
    </StyledFeatureContainer>
  );
}

const StyledMenuHeader = styled.div`
  width: 100%;
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
  isClicked: boolean;
}>(
  ({ isClicked, theme }) => `
  display: flex;
  gap: 2rem;
  cursor: pointer;
  padding: 2rem;

  ${
    isClicked &&
    `
    background: ${theme.colors.GRAY_100}; 
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;`
  };

  &:hover {
    background: ${!isClicked && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

const StyledAppointmentMenu = styled.div<{
  isClicked: boolean;
}>(
  ({ isClicked, theme }) => `
  display: flex;
  gap: 2rem;
  cursor: pointer;
  padding: 2rem;
  
  ${
    isClicked &&
    `
  background: ${theme.colors.GRAY_100}; 
  border-top-left-radius: 4rem; 
  border-bottom-left-radius: 4rem;
  `
  };

  &:hover {
    background: ${!isClicked && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

export default SidebarFeatureMenu;
