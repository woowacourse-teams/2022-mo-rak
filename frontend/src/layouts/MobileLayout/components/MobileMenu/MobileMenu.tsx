import {
  StyledContainer,
  StyledMenuIcon,
  StyledMenuListContainer,
  StyledCloseButton,
  StyledBottomMenu
} from './MobileMenu.styles';
import { getGroups } from '../../../../api/group';
import Home from '../../../../assets/home.svg';
import MenuImg from '../../../../assets/menu.svg';
import Poll from '../../../../assets/poll.svg';
import CloseButton from '../../../../assets/close-button.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';
import { useNavigate } from 'react-router-dom';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';
import { GroupInterface } from '../../../../types/group';
import { Menu } from '../../../../types/menu';
import Divider from '../../../../components/Divider/Divider';
import MobileFeaturesMenu from '../MobileFeaturesMenu/MobileFeaturesMenu';
import MobileMembersProfileMenu from '../MobileMembersProfileMenu/MobileMembersProfileMenu';
import MobileSlackMenu from '../MobileSlackMenu/MobileSlackMenu';
import MobileInvitationMenu from '../MobileInvitationMenu/MobileInvitationMenu';
import MobileLogoutMenu from '../MobileLogoutMenu/MobileLogoutMenu';
import MobileGroupsMenu from '../MobileGroupsMenu/MobileGroupsMenu';
import MobileMenuModals from '../MobileMenuModals/MobileMenuModals';

import { useState, useEffect } from 'react';

interface Props {
  groupCode: GroupInterface['code'];
}

function MobileMenu({ groupCode }: Props) {
  const { activeMenu } = useMenuContext();
  const dispatch = useMenuDispatchContext();
  const navigate = useNavigate();
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);
  const [isVisibleListMenu, setIsVisibleListMenu] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [activeModalMenu, setActiveModalMenu] = useState<null | string>(null);

  const handleCloseListMenu = () => {
    setIsVisibleListMenu(false);
  };

  const handleActiveListMenu = () => {
    dispatch({ type: 'SET_ACTIVE_MENU', payload: 'list' });
    setIsVisibleListMenu(true);
  };

  // TODO: 함수 역할에 맞게 분리 (navigate 역할 분리)
  const handleActiveMenu = (menu: Menu) => () => {
    dispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
    if (menu === 'main') {
      navigate(`/groups/${groupCode}`);
      return;
    }
    navigate(`/groups/${groupCode}/${menu}`);
  };

  const handleActiveModalMenu = (menu: null | string) => () => {
    setActiveModalMenu(menu);
    setIsVisibleListMenu(false);
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
        <button onClick={handleActiveMenu('main')}>
          <StyledMenuIcon src={Home} isActive={activeMenu === 'main'} alt="main-page" />
        </button>
        <button onClick={handleActiveListMenu}>
          <StyledMenuIcon src={MenuImg} alt="메뉴 목록" />
        </button>
        <button onClick={handleActiveMenu('poll')}>
          <StyledMenuIcon src={Poll} isActive={activeMenu === 'poll'} alt="투표하기 메뉴" />
        </button>
        <button onClick={handleActiveMenu('appointment')}>
          <StyledMenuIcon
            src={Appointment}
            isActive={activeMenu === 'appointment'}
            alt="약속잡기 메뉴"
          />
        </button>
        <button onClick={handleActiveMenu('role')}>
          <StyledMenuIcon src={Role} isActive={activeMenu === 'role'} alt="역할정하기 메뉴" />
        </button>
      </StyledContainer>

      {/* 메뉴리스트 */}
      <StyledMenuListContainer isVisible={isVisibleListMenu}>
        <StyledCloseButton onClick={handleCloseListMenu}>
          <img src={CloseButton} alt="메뉴닫기" />
        </StyledCloseButton>
        <MobileGroupsMenu
          onClickCloseListMenu={handleCloseListMenu}
          onClickMenu={handleActiveModalMenu}
          groupCode={groupCode}
          groups={groups}
        />

        <Divider />

        <MobileFeaturesMenu onClickCloseListMenu={handleCloseListMenu} groupCode={groupCode} />

        <Divider />
        <MobileMembersProfileMenu groupCode={groupCode} />

        <Divider />
        <StyledBottomMenu>
          <MobileSlackMenu onClick={handleActiveModalMenu('slack')} />
          <MobileInvitationMenu groupCode={groupCode} />
          <MobileLogoutMenu />
        </StyledBottomMenu>
      </StyledMenuListContainer>

      {/* TODO: 모달이 모여있음  */}
      {/* TODO: portal 사용 */}
      <MobileMenuModals
        activeModalMenu={activeModalMenu}
        closeModal={handleActiveModalMenu(null)}
        groupCode={groupCode}
      />
    </>
  );
}

export default MobileMenu;
