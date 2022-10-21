import Avatar from '../../../../components/Avatar/Avatar';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import {
  StyledContainer,
  StyledMenuHeader,
  StyledUsername,
  StyledGroupMembersContainer,
  StyledEditIcon,
  StyledUsernameContainer
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
          <FlexContainer key={`${name}-${profileUrl}`} alignItems="center" gap="2rem">
            <Avatar profileUrl={profileUrl} width="4rem" />
            <StyledUsernameContainer>
              <StyledUsername>{name}</StyledUsername>
              {id === authState.id && (
                <StyledEditIcon src={Edit} onClick={handleOpenEditUsernameModal} />
              )}
            </StyledUsernameContainer>
          </FlexContainer>
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
