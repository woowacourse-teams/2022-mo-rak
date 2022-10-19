import { StyledContainer } from './NavbarDrawerBottomSection.styles';
import NavbarSlackMenu from '../NavbarSlackMenu/NavbarSlackMenu';
import NavbarInvitationMenu from '../NavbarInvitationMenu/NavbarInvitationMenu';
import NavbarLogoutMenu from '../NavbarLogoutMenu/NavbarLogoutMenu';
import { Group } from '../../../../types/group';

type Props = {
  onClickMenu: (menu: null | string) => () => void;
  groupCode: Group['code'];
};

function NavbarDrawerBottomSection({ onClickMenu, groupCode }: Props) {
  return (
    <StyledContainer>
      <NavbarSlackMenu onClick={onClickMenu('slack')} />
      <NavbarInvitationMenu groupCode={groupCode} />
      <NavbarLogoutMenu />
    </StyledContainer>
  );
}

export default NavbarDrawerBottomSection;
