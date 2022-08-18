import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getGroupMembers } from '../../api/group';
import { GroupInterface, MemberInterface } from '../../types/group';
import Avatar from '../@common/Avatar/Avatar';
import FlexContainer from '../@common/FlexContainer/FlexContainer';

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
      <StyledMenuHeader>
        멤버 목록 (
        {groupMembersCount}
        )
      </StyledMenuHeader>
      <StyledContainer>
        <FlexContainer flexDirection="column" gap="1.6rem">
          {groupMembers.map(({ profileUrl, name }) => (
            <FlexContainer alignItems="center" gap="2rem">
              <Avatar profileUrl={profileUrl} name="" width="4rem" gap="0" />
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
  width: 100%;
  font-size: 1.7rem; // TODO: 4단위로 변경 
  text-align: left;
  margin-bottom: 2rem;
`;

const StyledName = styled.div`
  font-size: 1.6rem;
`;

const StyledContainer = styled.div`
  overflow-y: auto;
  height: 36rem;
  
`;

export default SidebarMembersProfileMenu;
