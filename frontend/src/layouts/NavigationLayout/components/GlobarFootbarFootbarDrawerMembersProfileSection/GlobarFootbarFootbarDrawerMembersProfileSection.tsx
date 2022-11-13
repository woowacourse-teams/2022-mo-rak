import { useEffect, useState } from 'react';
import { getGroupMembers } from '@/apis/group';
import { Group, Member } from '@/types/group';
import Avatar from '@/components/Avatar/Avatar';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import {
  StyledMenuHeader,
  StyledName,
  StyledContainer,
  StyledMembersProfileContainer
} from '@/layouts/NavigationLayout/components/GlobarFootbarFootbarDrawerMembersProfileSection/GlobarFootbarFootbarDrawerMembersProfileSection.styles';

type Props = {
  groupCode: Group['code'];
};

function GlobalFootbarFootbarDrawerMembersProfileSection({ groupCode }: Props) {
  const [groupMembers, setGroupMembers] = useState<Array<Member>>([]);
  const groupMembersCount = groupMembers.length;

  useEffect(() => {
    (async () => {
      const res = await getGroupMembers(groupCode);
      setGroupMembers(res.data);
    })();
  }, [groupCode]);

  return (
    <StyledContainer>
      <StyledMenuHeader>멤버 목록 ({groupMembersCount})</StyledMenuHeader>
      <StyledMembersProfileContainer>
        <FlexContainer flexDirection="column" gap="1.6rem">
          {groupMembers.map(({ profileUrl, name }) => (
            <FlexContainer key={`${name}-${profileUrl}`} alignItems="center" gap="2rem">
              <Avatar profileUrl={profileUrl} width="4rem" name="" />
              <StyledName>{name}</StyledName>
            </FlexContainer>
          ))}
        </FlexContainer>
      </StyledMembersProfileContainer>
    </StyledContainer>
  );
}

export default GlobalFootbarFootbarDrawerMembersProfileSection;
