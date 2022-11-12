import SidebarEditUsernameModal from '@/layouts/NavigationLayout/components/SidebarEditUsernameModal/SidebarEditUsernameModal';
import {
  StyledContainer,
  StyledEditIcon,
  StyledGroupMemberContainer,
  StyledGroupMembersContainer,
  StyledMenuHeader,
  StyledUsername,
  StyledUsernameContainer
} from '@/layouts/NavigationLayout/components/SidebarMembersProfileMenu/SidebarMembersProfileMenu.styles';

import Avatar from '@/components/Avatar/Avatar';

import useAuthContext from '@/hooks/useAuthContext';
import useGroupMembersContext from '@/hooks/useGroupMembersContext';
import useModal from '@/hooks/useModal';

import editImg from '@/assets/edit.svg';

function SidebarMembersProfileMenu() {
  const { groupMembers } = useGroupMembersContext();
  const groupMembersCount = groupMembers.length;
  const authState = useAuthContext();
  const [isEditUsernameModalVisible, handleOpenEditUsernameModal, handleCloseEditUsernameModal] =
    useModal();

  return (
    <StyledContainer>
      <StyledMenuHeader>멤버 목록 ({groupMembersCount})</StyledMenuHeader>
      <StyledGroupMembersContainer>
        {groupMembers.map(({ id, profileUrl, name }) => (
          <StyledGroupMemberContainer key={`${name}-${profileUrl}`}>
            <Avatar profileUrl={profileUrl} width="4rem" />
            <StyledUsernameContainer>
              <StyledUsername>{name}</StyledUsername>
              {id === authState.id && (
                <StyledEditIcon src={editImg} onClick={handleOpenEditUsernameModal} />
              )}
            </StyledUsernameContainer>
          </StyledGroupMemberContainer>
        ))}
      </StyledGroupMembersContainer>
      <SidebarEditUsernameModal
        isVisible={isEditUsernameModalVisible}
        close={handleCloseEditUsernameModal}
      />
    </StyledContainer>
  );
}

export default SidebarMembersProfileMenu;
