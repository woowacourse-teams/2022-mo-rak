import { useState } from 'react';

import { StyledContainer, StyledCloseButton } from './GlobalFootbarFootbarDrawer.styles';
import CloseButton from '../../../../assets/close-button.svg';
import Divider from '../../../../components/Divider/Divider';
import GlobalFootbarFootbarDrawerFeaturesSection from '../GlobalFootbarFootbarDrawerFeaturesSection/GlobalFootbarFootbarDrawerFeaturesSection';
import GlobalFootbarFootbarDrawerMembersProfileSection from '../GlobarFootbarFootbarDrawerMembersProfileSection/GlobarFootbarFootbarDrawerMembersProfileSection';
import GlobalFootbarFootbarDrawerGroupsSection from '../GlobalFootbarFootbarDrawerGroupsSection/GlobalFootbarFootbarDrawerGroupsSection';
import GlobalFootbarFootbarDrawerBottomSection from '../GlobalFootbarFootbarDrawerBottomSection/GlobalFootbarFootbarDrawerBottomSection';
import NavbarDrawerModals from '../GlobalFootbarFootbarDrawerModals/GlobalFootbarFootbarDrawerModals';
import useNavigationBarContext from '../../../../hooks/useNavigationBarContext';
import useNavigationBarDispatchContext from '../../../../hooks/useNavigationBarDispatchContext';
import { Group } from '../../../../types/group';

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
          <img src={CloseButton} alt="메뉴닫기" />
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
      {/* TODO: 모달이 기존과 같음 */}
      <NavbarDrawerModals
        activeModalMenu={activeModalMenu}
        closeModal={handleSetActiveModalMenu(null)}
        groupCode={groupCode}
      />
    </>
  );
}

export default GlobalFootbarFootbarDrawer;
