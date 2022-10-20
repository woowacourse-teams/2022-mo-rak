import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Logo from '../../../../assets/logo.svg';

import { getGroups } from '../../../../api/group';
import { Group } from '../../../../types/group';

import Divider from '../../../../components/Divider/Divider';
import SidebarGroupsMenu from '../SidebarGroupsMenu/SidebarGroupsMenu';
import SidebarMenuModals from '../SidebarMenuModals/SidebarMenuModals';

import SidebarMembersProfileMenu from '../SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeaturesMenu from '../SidebarFeaturesMenu/SidebarFeaturesMenu';
import SidebarInvitationMenu from '../SidebarInvitationMenu/SidebarInvitationMenu';
import SidebarSlackMenu from '../SidebarSlackMenu/SidebarSlackMenu';
import SidebarLogoutMenu from '../SidebarLogoutMenu/SidebarLogoutMenu';
import { StyledContainer, StyledLogo, StyledBottomMenu } from './Sidebar.styles';
import MarginContainer from '../../../../components/MarginContainer/MarginContainer';

function Sidebar() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<Group>>([]);
  const [activeModalMenu, setActiveModalMenu] = useState<null | string>(null);
  const { groupCode } = useParams() as { groupCode: Group['code'] };

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleActiveModalMenu = (menu: null | string) => () => {
    setActiveModalMenu(menu);
  };

  useEffect(() => {
    (async () => {
      try {
        const res = await getGroups();
        setGroups(res.data);
        setIsLoading(false);
      } catch (err) {
        setIsLoading(true);
      }
    })();
  }, [groupCode]);

  if (isLoading) return <div>로딩중</div>;

  return (
    <>
      <StyledContainer>
        <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
        <SidebarGroupsMenu
          onClickMenu={handleActiveModalMenu}
          groupCode={groupCode}
          groups={groups}
        />

        <Divider />
        {/* NOTE: SidebarFeatureSection */}
        <MarginContainer margin="2.8rem 0">
          <SidebarFeaturesMenu groupCode={groupCode} />
        </MarginContainer>

        <Divider />
        {/* NOTE: SidebarMemberSection */}
        <MarginContainer margin="2.8rem 0">
          <SidebarMembersProfileMenu />
        </MarginContainer>

        <StyledBottomMenu>
          <SidebarSlackMenu onClick={handleActiveModalMenu('slack')} />
          <SidebarInvitationMenu groupCode={groupCode} />
          <SidebarLogoutMenu />
        </StyledBottomMenu>
      </StyledContainer>

      {/* TODO: 모달이 모여있음  */}
      {/* TODO: portal 사용 */}
      <SidebarMenuModals
        activeModalMenu={activeModalMenu}
        closeModal={handleActiveModalMenu(null)}
        groupCode={groupCode}
      />
    </>
  );
}

export default Sidebar;
