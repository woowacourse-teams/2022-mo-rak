import { StyledContainer } from './GlobalFootbarFootbarDrawerBottomSection.styles';
import GlobalFootbarFootbarDrawerBottomSectionSlackMenu from '../GlobalFootbarFootbarDrawerBottomSectionSlackMenu/GlobalFootbarFootbarDrawerBottomSectionSlackMenu';
import GlobalFootbarFootbarDrawerBottomSectionInvitationMenu from '../GlobalFootbarFootbarDrawerBottomSectionInvitationMenu/GlobalFootbarFootbarDrawerBottomSectionInvitationMenu';
import GlobalFootbarFootbarDrawerBottomSectionLogoutMenu from '../GlobalFootbarFootbarDrawerBottomSectionLogoutMenu/GlobalFootbarFootbarDrawerBottomSectionLogoutMenu';
import { Group } from '@/types/group';

type Props = {
  onClickMenu: (menu: null | string) => () => void;
  groupCode: Group['code'];
};

function GlobalFootbarFootbarDrawerBottomSection({ onClickMenu, groupCode }: Props) {
  return (
    <StyledContainer>
      <GlobalFootbarFootbarDrawerBottomSectionSlackMenu onClick={onClickMenu('slack')} />
      <GlobalFootbarFootbarDrawerBottomSectionInvitationMenu groupCode={groupCode} />
      <GlobalFootbarFootbarDrawerBottomSectionLogoutMenu />
    </StyledContainer>
  );
}

export default GlobalFootbarFootbarDrawerBottomSection;
