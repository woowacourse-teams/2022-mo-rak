import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Logo from '../../../../assets/logo.svg';

import { getGroups } from '../../../../api/group';
import { Group } from '../../../../types/group';

import SidebarGroupsMenu from '../SidebarGroupsMenu/SidebarGroupsMenu';
import SidebarModals from '../SidebarModals/SidebarModals';

import SidebarMembersProfileMenu from '../SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeaturesMenu from '../SidebarFeaturesMenu/SidebarFeaturesMenu';
import SidebarInvitationMenu from '../SidebarInvitationMenu/SidebarInvitationMenu';
import SidebarSlackMenu from '../SidebarSlackMenu/SidebarSlackMenu';
import SidebarLogoutMenu from '../SidebarLogoutMenu/SidebarLogoutMenu';
import {
  StyledContainer,
  StyledLogo,
  StyledBottomMenu,
  StyledLogoContainer
} from './Sidebar.styles';
import Spinner from '../../../../components/Spinner/Spinner';

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
              <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
            </StyledLogoContainer>
            <SidebarGroupsMenu
              onClickMenu={handleActiveModal}
              groupCode={groupCode}
              groups={groups}
            />
            <SidebarFeaturesMenu groupCode={groupCode} />
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
