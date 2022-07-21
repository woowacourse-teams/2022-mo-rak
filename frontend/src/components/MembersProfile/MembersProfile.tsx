import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getGroupMembers } from '../../api/group';
import { GroupInterface, MemberInterface } from '../../types/group';

interface Props {
  groupCode: GroupInterface['code'];
}

// TODO: 컴포넌트 이름 생각해보자 profile은 단수
function MembersProfile({ groupCode }: Props) {
  const navigate = useNavigate();
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);

  useEffect(() => {
    const fetchGroupMembers = async () => {
      try {
        if (groupCode) {
          const res = await getGroupMembers(groupCode);

          setGroupMembers(res);
        }
      } catch (err) {
        if (err instanceof Error) {
          const statusCode = err.message;

          if (statusCode === '401') {
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
        <StyledUserProfile>
          <StyledUserImage src={profileUrl} />
          <StyledUserName>{name}</StyledUserName>
        </StyledUserProfile>
      ))}
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  gap: 1.2rem;
  margin: 2rem 0;
`;

const StyledUserProfile = styled.div`
  width: 4.4rem;
  cursor: pointer;
  position: relative;
`;

const StyledUserImage = styled.img`
  width: 100%;
  height: 4.4rem;
  border-radius: 100%;
  margin-bottom: 0.4rem;
`;

const StyledUserName = styled.div`
  text-align: center;
`;

export default MembersProfile;
