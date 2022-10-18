import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Logo from '../../assets/logo.svg';

import { getGroups } from '../../api/group';
import { GroupInterface } from '../../types/group';

import Divider from '../../components/Divider/Divider';
import SidebarGroupsMenu from './components/SidebarGroupsMenu/SidebarGroupsMenu';
import SidebarMenuModals from './components/SidebarMenuModals/SidebarMenuModals';

import SidebarMembersProfileMenu from './components/SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeaturesMenu from './components/SidebarFeaturesMenu/SidebarFeaturesMenu';
import SidebarInvitationMenu from './components/SidebarInvitationMenu/SidebarInvitationMenu';
import SidebarSlackMenu from './components/SidebarSlackMenu/SidebarSlackMenu';
import SidebarLogoutMenu from './components/SidebarLogoutMenu/SidebarLogoutMenu';
import { StyledContainer, StyledLogo, StyledBottomMenu } from './SidebarLayout.styles';

function SidebarLayout() {
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

export default SidebarLayout;
