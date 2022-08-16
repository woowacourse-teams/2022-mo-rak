import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useNavigate, useParams } from 'react-router-dom';
import Logo from '../../assets/logo.svg';

import { getGroups } from '../../api/group';
import { GroupInterface } from '../../types/group';
import Divider from '../@common/Divider/Divider';
import SidebarGroupMenu from '../SidebarGroupMenu/SidebarGroupMenu';

import SidebarMembersProfileMenu from '../SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeatureMenu from '../SidebarFeatureMenu/SidebarFeatureMenu';
import SidebarInvitationMenu from '../SidebarInvitationMenu/SidebarInvitationMenu';
import SidebarSlackMenu from '../SidebarSlackMenu/SidebarSlackMenu';

function Sidebar() {
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);

  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };

  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

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

  if (isLoading) return <div>로딩중</div>;

  return (
    <StyledContainer>
      <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />

      {/* 그룹 */}
      <SidebarGroupMenu groupCode={groupCode} groups={groups} />

      {/* 기능 */}
      <Divider />
      <SidebarFeatureMenu groupCode={groupCode} />

      {/* 멤버 목록 */}
      <Divider />
      <SidebarMembersProfileMenu groupCode={groupCode} />

      <StyledBottomMenu>
        {/* 슬랙연동 */}
        <SidebarSlackMenu />

        {/* 초대링크 */}
        <SidebarInvitationMenu groupCode={groupCode} />
      </StyledBottomMenu>

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

const StyledBottomMenu = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  position: absolute;
  bottom: 4rem;
`;

export default Sidebar;
