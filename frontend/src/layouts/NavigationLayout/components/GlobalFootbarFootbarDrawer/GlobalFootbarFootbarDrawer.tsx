import { useState } from 'react';

import {
  StyledCloseButton,
  StyledContainer
} from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawer/GlobalFootbarFootbarDrawer.styles';
import GlobalFootbarFootbarDrawerBottomSection from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerBottomSection/GlobalFootbarFootbarDrawerBottomSection';
import GlobalFootbarFootbarDrawerFeaturesSection from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerFeaturesSection/GlobalFootbarFootbarDrawerFeaturesSection';
import GlobalFootbarFootbarDrawerGroupsSection from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerGroupsSection/GlobalFootbarFootbarDrawerGroupsSection';
import NavbarDrawerModals from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerModals/GlobalFootbarFootbarDrawerModals';
import GlobalFootbarFootbarDrawerMembersProfileSection from '@/layouts/NavigationLayout/components/GlobarFootbarFootbarDrawerMembersProfileSection/GlobarFootbarFootbarDrawerMembersProfileSection';

import Divider from '@/components/Divider/Divider';

import useNavigationBarContext from '@/hooks/useNavigationBarContext';
import useNavigationBarDispatchContext from '@/hooks/useNavigationBarDispatchContext';

import closeButtonImg from '@/assets/close-button.svg';
import { Group } from '@/types/group';

// TODO: props drilling 발생 (groups)
type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
};

function GlobalFootbarFootbarDrawer({ groupCode, groups }: Props) {
  const { isDrawerVisible } = useNavigationBarContext();
  const navigationBarDispatch = useNavigationBarDispatchContext();
  const [activeModalMenu, setActiveModalMenu] = useState<null | string>(null);

  const handleCloseDrawer = () => {
    navigationBarDispatch({ type: 'SET_IS_DRAWER_VISIBLE', payload: false });
  };

  const handleSetActiveModalMenu = (menu: null | string) => () => {
    setActiveModalMenu(menu);
    navigationBarDispatch({ type: 'SET_IS_DRAWER_VISIBLE', payload: false });
  };

  return (
    <>
      <StyledContainer isVisible={isDrawerVisible}>
        <StyledCloseButton onClick={handleCloseDrawer}>
          <img src={closeButtonImg} alt="메뉴닫기" />
        </StyledCloseButton>

        <GlobalFootbarFootbarDrawerGroupsSection
          closeDrawer={handleCloseDrawer}
          onClickMenu={handleSetActiveModalMenu}
          groupCode={groupCode}
          groups={groups}
        />

        <Divider />
        <GlobalFootbarFootbarDrawerFeaturesSection
          closeDrawer={handleCloseDrawer}
          groupCode={groupCode}
        />

        <Divider />
        <GlobalFootbarFootbarDrawerMembersProfileSection groupCode={groupCode} />

        <Divider />
        <GlobalFootbarFootbarDrawerBottomSection
          onClickMenu={handleSetActiveModalMenu}
          groupCode={groupCode}
        />
      </StyledContainer>

      {/* TODO: 모달이 모여있음  */}
      {/* TODO: portal 사용 */}
      <NavbarDrawerModals
        activeModalMenu={activeModalMenu}
        closeModal={handleSetActiveModalMenu(null)}
        groupCode={groupCode}
      />
    </>
  );
}

export default GlobalFootbarFootbarDrawer;
