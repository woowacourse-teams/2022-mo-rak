import React, { useState, MouseEventHandler, useEffect } from 'react';
import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';
import Logo from '../../assets/logo.svg';
import Poll from '../../assets/person-check.svg';
import Appointment from '../../assets/calendar-clock.svg';
import Plus from '../../assets/plus.svg';
import { createInvitationCode, getGroups } from '../../api/group';
import { writeClipboard } from '../../utils/clipboard';
import { GroupInterface } from '../../types/group';
import Divider from '../@common/Divider/Divider';
import SidebarGroupMenu from '../SidebarGroupMenu/SidebarGroupMenu';

import FlexContainer from '../@common/FlexContainer/FlexContainer';
import SidebarMembersProfileMenu from '../SidebarMembersProfileMenu/SidebarMembersProfileMenu';

interface Props {
  groupCode: GroupInterface['code'];
  handleSetClickedMenu: (menu: string) => MouseEventHandler<HTMLDivElement>;
  clickedMenu: string;
}

function Sidebar({ groupCode, handleSetClickedMenu, clickedMenu }: Props) {
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const res = await getGroups();
        setGroups(res.data);
        setIsLoading(false);
      } catch (err) {
        alert(err);
        setIsLoading(true);
      }
    };

    fetchGroups();
  }, []);

  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleCopyInviationCode = async () => {
    try {
      if (groupCode) {
        const res = await createInvitationCode(groupCode);
        const invitationCode = res.headers.location.split('groups/in')[1];
        const invitationLink = `
        링크를 클릭하거나, 참가 코드를 입력해주세요😀
        url: ${process.env.CLIENT_URL}/invite/${invitationCode}}
        코드: ${invitationCode}
        `;

        writeClipboard(invitationLink);
        alert('초대링크가 클립보드에 복사되었습니다💌');
      }
    } catch (err) {
      alert(err);
    }
  };

  if (isLoading) return <div>로딩중</div>;

  return (
    <StyledContainer>
      <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
      {/* 그룹 */}
      <SidebarGroupMenu groupCode={groupCode} groups={groups} />

      {/* 기능 */}
      <Divider />
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

      {/* 멤버 목록 */}
      <Divider />
      <SidebarMembersProfileMenu groupCode={groupCode} />

      {/* 초대링크 */}
      <StyledInvitationLink onClick={handleCopyInviationCode}>
        <img src={Plus} alt="inivation-link" />
        <StyledInviteText>새로운 멤버 초대</StyledInviteText>
      </StyledInvitationLink>
    </StyledContainer>
  );
}

const StyledContainer = styled.div(
  ({ theme }) => `
  z-index: 1; 
  width: 36.4rem;
  height: 100vh;
  position: sticky;
  top: 0;
  border-right: 0.1rem solid ${theme.colors.GRAY_200};
  background: ${theme.colors.WHITE_100};
  padding-left: 4rem;
  gap: 2rem;
  border: none;
`
);

const StyledLogo = styled.img`
  display: block;
  margin: 2rem auto;  
  width: 12rem;
  cursor: pointer;
  padding-right: 4rem;
`;

// 그룹

const StyledMenuHeader = styled.div`
width: 100%;
font-size: 2rem;
text-align: left;
margin-bottom: 2rem;
`;

// 기능
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

// 초대링크
const StyledInvitationLink = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  position: absolute;
  bottom: 3.6rem;
  left: 3.6rem;
  gap: 1.2rem;
  font-size: 1.6rem;
`;

const StyledInviteText = styled.p`
  font-size: 2rem;
`;

export default Sidebar;
