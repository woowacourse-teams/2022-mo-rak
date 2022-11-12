import { StyledContainer } from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerBottomSection/GlobalFootbarFootbarDrawerBottomSection.styles';
import GlobalFootbarFootbarDrawerBottomSectionInvitationMenu from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerBottomSectionInvitationMenu/GlobalFootbarFootbarDrawerBottomSectionInvitationMenu';
import GlobalFootbarFootbarDrawerBottomSectionLogoutMenu from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerBottomSectionLogoutMenu/GlobalFootbarFootbarDrawerBottomSectionLogoutMenu';
import GlobalFootbarFootbarDrawerBottomSectionSlackMenu from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerBottomSectionSlackMenu/GlobalFootbarFootbarDrawerBottomSectionSlackMenu';

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
