import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Logo from '../../../../assets/logo.svg';

import { getGroups } from '../../../../api/group';
import { GroupInterface } from '../../../../types/group';

import Divider from '../../../../components/Divider/Divider';
import SidebarGroupsMenu from '../SidebarGroupsMenu/SidebarGroupsMenu';
import SidebarMenuModals from '../SidebarMenuModals/SidebarMenuModals';

import SidebarMembersProfileMenu from '../SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeaturesMenu from '../SidebarFeaturesMenu/SidebarFeaturesMenu';
import SidebarInvitationMenu from '../SidebarInvitationMenu/SidebarInvitationMenu';
import SidebarSlackMenu from '../SidebarSlackMenu/SidebarSlackMenu';
import SidebarLogoutMenu from '../SidebarLogoutMenu/SidebarLogoutMenu';
import { StyledContainer, StyledLogo, StyledBottomMenu } from './Sidebar.styles';

function Sidebar() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);
  const [activeModalMenu, setActiveModalMenu] = useState<null | string>(null);
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };

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
        <SidebarFeaturesMenu groupCode={groupCode} />

        <Divider />
        <SidebarMembersProfileMenu groupCode={groupCode} />

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
