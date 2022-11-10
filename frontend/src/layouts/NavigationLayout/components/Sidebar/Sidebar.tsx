import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import serviceLogoImg from '@/assets/service-logo.svg';

import { getGroups } from '@/apis/group';
import { Group } from '@/types/group';

import SidebarGroupsMenu from '@/layouts/NavigationLayout/components/SidebarGroupsMenu/SidebarGroupsMenu';
import SidebarModals from '@/layouts/NavigationLayout/components/SidebarModals/SidebarModals';

import SidebarMembersProfileMenu from '@/layouts/NavigationLayout/components/SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeaturesMenu from '@/layouts/NavigationLayout/components/SidebarFeaturesMenu/SidebarFeaturesMenu';
import SidebarInvitationMenu from '@/layouts/NavigationLayout/components/SidebarInvitationMenu/SidebarInvitationMenu';
import SidebarSlackMenu from '@/layouts/NavigationLayout/components/SidebarSlackMenu/SidebarSlackMenu';
import SidebarLogoutMenu from '@/layouts/NavigationLayout/components/SidebarLogoutMenu/SidebarLogoutMenu';
import {
  StyledContainer,
  StyledLogo,
  StyledBottomMenu,
  StyledLogoContainer
} from '@/layouts/NavigationLayout/components/Sidebar/Sidebar.styles';
import Divider from '@/components/Divider/Divider';
import Spinner from '@/components/Spinner/Spinner';

function Sidebar() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<Group>>([]);
  const [activeModal, setActiveModal] = useState<null | string>(null);
  const { groupCode } = useParams() as { groupCode: Group['code'] };

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleActiveModal = (menu: null | string) => () => {
    setActiveModal(menu);
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

  return (
    <>
      <StyledContainer>
        {isLoading ? (
          <Spinner width="40%" placement="center" />
        ) : (
          <>
            <StyledLogoContainer>
              <StyledLogo
                src={serviceLogoImg}
                alt={serviceLogoImg}
                onClick={handleNavigate(`/groups/${groupCode}`)}
              />
            </StyledLogoContainer>
            <SidebarGroupsMenu
              onClickMenu={handleActiveModal}
              groupCode={groupCode}
              groups={groups}
            />
            <Divider />

            <SidebarFeaturesMenu groupCode={groupCode} />
            <Divider />

            <SidebarMembersProfileMenu />
            <StyledBottomMenu>
              <SidebarSlackMenu onClick={handleActiveModal('slack')} />
              <SidebarInvitationMenu groupCode={groupCode} />
              <SidebarLogoutMenu />
            </StyledBottomMenu>
          </>
        )}
      </StyledContainer>

      {/* TODO: 모달이 모여있음  */}
      {/* TODO: portal 사용 */}
      <SidebarModals
        activeModal={activeModal}
        closeModal={handleActiveModal(null)}
        groupCode={groupCode}
      />
    </>
  );
}

export default Sidebar;
