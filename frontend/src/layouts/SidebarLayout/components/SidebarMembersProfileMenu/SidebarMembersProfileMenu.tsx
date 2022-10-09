import styled from '@emotion/styled';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getGroupMembers } from '../../../../api/group';
import { GroupInterface, MemberInterface } from '../../../../types/group';
import Avatar from '../../../../components/Avatar/Avatar';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

interface Props {
  groupCode: GroupInterface['code'];
}

function SidebarMembersProfileMenu({ groupCode }: Props) {
  const navigate = useNavigate();
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);
  const groupMembersCount = groupMembers.length;

  useEffect(() => {
    const fetchGroupMembers = async () => {
      try {
        if (groupCode) {
          const res = await getGroupMembers(groupCode);

          setGroupMembers(res.data);
        }
      } catch (err) {
        if (err instanceof Error) {
          const statusCode = err.message;
          if (statusCode === '404' || '403' || '400') {
            alert('그룹이 없어요~');
            navigate('/');
          }
        }
      }
    };

    fetchGroupMembers();
  }, [groupCode]);

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

const StyledMemberListContainer = styled.div`
  margin: 2.8rem 0;
`;

const StyledMenuHeader = styled.div`
  font-size: 1.6rem;
  text-align: left;
  margin-bottom: 2rem;
`;

const StyledName = styled.div`
  font-size: 1.6rem;
`;

const StyledContainer = styled.div`
  overflow-y: auto;
  max-height: 36rem;
`;

export default SidebarMembersProfileMenu;
