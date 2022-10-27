import Avatar from '../../../../components/Avatar/Avatar';
import {
  StyledContainer,
  StyledMenuHeader,
  StyledUsername,
  StyledGroupMembersContainer,
  StyledEditIcon,
  StyledUsernameContainer,
  StyledGroupMemberContainer
} from './SidebarMembersProfileMenu.styles';
import useGroupMembersContext from '../../../../hooks/useGroupMembersContext';
import Edit from '../../../../assets/edit.svg';
import useModal from '../../../../hooks/useModal';
import SidebarEditUsernameModal from '../SidebarEditUsernameModal/SidebarEditUsernameModal';
import useAuthContext from '../../../../hooks/useAuthContext';

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
                <StyledEditIcon src={Edit} onClick={handleOpenEditUsernameModal} />
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
