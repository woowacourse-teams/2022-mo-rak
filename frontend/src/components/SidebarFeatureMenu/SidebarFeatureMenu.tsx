import React, { MouseEventHandler } from 'react';
import styled from '@emotion/styled';
import Poll from '../../assets/person-check.svg';
import Appointment from '../../assets/calendar-clock.svg';
import FlexContainer from '../@common/FlexContainer/FlexContainer';

interface Props {
  handleSetClickedMenu: (menu: string) => MouseEventHandler<HTMLDivElement>;
  clickedMenu: string;
}

// TODO: props drilling 발생!
function SidebarFeatureMenu({ handleSetClickedMenu, clickedMenu }: Props) {
  return (
    <StyledFeatureContainer>
      <StyledMenuHeader>기능</StyledMenuHeader>
      <FlexContainer flexDirection="column">
        <StyledPollMenu onClick={handleSetClickedMenu('poll')} isClicked={clickedMenu === 'poll'}>
          <StyledPollIcon src={Poll} />
          <StyledFeatureTitle>투표하기</StyledFeatureTitle>
        </StyledPollMenu>
        <StyledAppointmentMenu onClick={handleSetClickedMenu('appointment')} isClicked={clickedMenu === 'appointment'}>
          <StyledAppointmentIcon src={Appointment} />
          <StyledFeatureTitle>약속잡기</StyledFeatureTitle>
        </StyledAppointmentMenu>
      </FlexContainer>
    </StyledFeatureContainer>
  );
}

const StyledMenuHeader = styled.div`
width: 100%;
font-size: 2rem;
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
  isClicked: boolean
}>(
  ({ isClicked, theme }) => `
  display: flex;
  gap: 2rem;
  cursor: pointer;
  padding: 2rem;

  ${isClicked && `
    background: ${theme.colors.GRAY_100}; 
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;`};

  &:hover {
    background: ${!isClicked && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

const StyledAppointmentMenu = styled.div<{
  isClicked: boolean
}>(
  ({ isClicked, theme }) => `
  display: flex;
  gap: 2rem;
  cursor: pointer;
  padding: 2rem;
  
  ${isClicked && `
  background: ${theme.colors.GRAY_100}; 
  border-top-left-radius: 4rem; 
  border-bottom-left-radius: 4rem;
  `};

  &:hover {
    background: ${!isClicked && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

export default SidebarFeatureMenu;
