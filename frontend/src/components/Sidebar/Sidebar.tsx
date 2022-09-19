import { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useNavigate, useParams } from 'react-router-dom';
import Logo from '../../assets/logo.svg';

import { getGroups } from '../../api/group';
import { GroupInterface } from '../../types/group';

import Divider from '../@common/Divider/Divider';
import SidebarGroupsMenu from '../SidebarGroupsMenu/SidebarGroupsMenu';
import SidebarMenuModals from '../SidebarMenuModals/SidebarMenuModals';

import SidebarMembersProfileMenu from '../SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeatureMenu from '../SidebarFeatureMenu/SidebarFeatureMenu';
import SidebarInvitationMenu from '../SidebarInvitationMenu/SidebarInvitationMenu';
import SidebarSlackMenu from '../SidebarSlackMenu/SidebarSlackMenu';

function Sidebar() {
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);
  const [activeModalMenu, setActiveModalMenu] = useState<null | string>(null);

  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };

  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleActiveGroupMenu = (menu: null | string) => () => {
    setActiveModalMenu(menu);
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
  }, [groupCode]);

  if (isLoading) return <div>로딩중</div>; 

  return (
    <>
      <StyledContainer>
        <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
        
        <SidebarGroupsMenu
          onClick={handleActiveGroupMenu}
          groupCode={groupCode}
          groups={groups}
        />

        <Divider />
        <SidebarFeatureMenu groupCode={groupCode} />

        <Divider />
        <SidebarMembersProfileMenu groupCode={groupCode} />

        <StyledBottomMenu>
          <SidebarSlackMenu onClick={handleActiveGroupMenu('slack')} /> 
          <SidebarInvitationMenu groupCode={groupCode} />
        </StyledBottomMenu>
      </StyledContainer>

      {/* TODO: 모달이 모여있음  */}
      <SidebarMenuModals
        activeModalMenu={activeModalMenu}
        closeModal={handleActiveGroupMenu(null)}
        groupCode={groupCode}
      />
    </>
  );
}

const StyledContainer = styled.div(
  ({ theme }) => `
  position: sticky;
  top: 0;
  width: 36.4rem;
  height: 100vh;
  z-index: 1; 
  background: ${theme.colors.WHITE_100};
  padding-left: 4rem;
  gap: 2rem;
`
);

const StyledLogo = styled.img`
  display: block;
  margin: 2rem auto;
  width: 16rem;
  cursor: pointer;
  padding-right: 4rem;
`;

const StyledBottomMenu = styled.div`
  position: absolute; 
  bottom: 4rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

export default Sidebar;
