import React, { useState, MouseEventHandler, useEffect } from 'react';
import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';
import Logo from '../../assets/logo.svg';

import { getGroups } from '../../api/group';
import { GroupInterface } from '../../types/group';
import Divider from '../@common/Divider/Divider';
import SidebarGroupMenu from '../SidebarGroupMenu/SidebarGroupMenu';

import SidebarMembersProfileMenu from '../SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeatureMenu from '../SidebarFeatureMenu/SidebarFeatureMenu';
import SidebarInvitationMenu from '../SidebarInvitationMenu/SidebarInvitationMenu';

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

  if (isLoading) return <div>로딩중</div>;

  return (
    <StyledContainer>
      <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />

      {/* 그룹 */}
      <SidebarGroupMenu groupCode={groupCode} groups={groups} />

      {/* 기능 */}
      <Divider />
      {/* TODO: props drilling 발생!  */}
      <SidebarFeatureMenu handleSetClickedMenu={handleSetClickedMenu} clickedMenu={clickedMenu} />

      {/* 멤버 목록 */}
      <Divider />
      <SidebarMembersProfileMenu groupCode={groupCode} />

      {/* 초대링크 */}
      <SidebarInvitationMenu groupCode={groupCode} />
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

export default Sidebar;
