import { StyledBottomMenu } from './NavbarBottomSection.styles';
import NavbarSlackMenu from '../NavbarSlackMenu/NavbarSlackMenu';
import NavbarInvitationMenu from '../NavbarInvitationMenu/NavbarInvitationMenu';
import NavbarLogoutMenu from '../NavbarLogoutMenu/NavbarLogoutMenu';
import { Group } from '../../../../types/group';

type Props = {
  onClickMenu: (menu: null | string) => () => void;
  groupCode: Group['code'];
};

function NavbarBottomSection({ onClickMenu, groupCode }: Props) {
  return (
    <StyledBottomMenu>
      <NavbarSlackMenu onClick={onClickMenu('slack')} />
      <NavbarInvitationMenu groupCode={groupCode} />
      <NavbarLogoutMenu />
    </StyledBottomMenu>
  );
}

export default NavbarBottomSection;
