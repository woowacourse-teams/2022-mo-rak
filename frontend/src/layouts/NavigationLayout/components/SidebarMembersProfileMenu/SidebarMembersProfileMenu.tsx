import Avatar from '../../../../components/Avatar/Avatar';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import {
  StyledMemberListContainer,
  StyledMenuHeader,
  StyledName,
  StyledContainer
} from './SidebarMembersProfileMenu.styles';
import useGroupMembersContext from '../../../../hooks/useGroupMembersContext';

function SidebarMembersProfileMenu() {
  const { groupMembers } = useGroupMembersContext();
  const groupMembersCount = groupMembers.length;

  return (
    <StyledMemberListContainer>
      <StyledMenuHeader>멤버 목록 ({groupMembersCount})</StyledMenuHeader>
      <StyledContainer>
        <FlexContainer flexDirection="column" gap="1.6rem">
          {groupMembers.map(({ profileUrl, name }) => (
            <FlexContainer key={`${name}-${profileUrl}`} alignItems="center" gap="2rem">
              <Avatar profileUrl={profileUrl} width="4rem" name="" />
              <StyledName>{name}</StyledName>
            </FlexContainer>
          ))}
        </FlexContainer>
      </StyledContainer>
    </StyledMemberListContainer>
  );
}

export default SidebarMembersProfileMenu;
