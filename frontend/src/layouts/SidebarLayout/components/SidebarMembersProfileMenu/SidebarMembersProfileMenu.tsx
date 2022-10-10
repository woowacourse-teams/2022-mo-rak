import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getGroupMembers } from '../../../../api/group';
import { GroupInterface, MemberInterface } from '../../../../types/group';
import Avatar from '../../../../components/Avatar/Avatar';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import {
  StyledMemberListContainer,
  StyledMenuHeader,
  StyledName,
  StyledContainer
} from './SidebarMembersProfileMenu.styles';

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

          if (statusCode === '401') {
            alert('로그인 해주세요~');
            navigate('/');
          }

          if (statusCode === '404' || '403' || '400') {
            alert('그룹이 없어요~');
            navigate('/');
          }
        }
        console.log(err);
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

export default SidebarMembersProfileMenu;
