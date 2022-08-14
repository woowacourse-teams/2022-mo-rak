import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getGroupMembers } from '../../api/group';
import { GroupInterface, MemberInterface } from '../../types/group';
import Avatar from '../@common/Avatar/Avatar';

interface Props {
  groupCode: GroupInterface['code'];
}

function MembersProfile({ groupCode }: Props) {
  const navigate = useNavigate();
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);

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
    <StyledContainer>
      {groupMembers.map(({ profileUrl, name }) => (
        <Avatar profileUrl={profileUrl} name={name} />
      ))}
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  gap: 1.2rem;
  margin: 2rem 0;
`;

export default MembersProfile;
