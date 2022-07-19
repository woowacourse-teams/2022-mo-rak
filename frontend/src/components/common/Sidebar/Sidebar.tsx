import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { Link, useParams } from 'react-router-dom';
import Logo from '../../../assets/logo.svg';
import LinkIcon from '../../../assets/linkIcon.svg';
import { createInvitationCode, getGroups } from '../../../api/group';
import { writeClipboard } from '../../../utils/clipboard';
import { GroupInterface } from '../../../types/group';

function Sidebar() {
  // TODO: groupCode가 무조건 존재하나?
  const { groupCode } = useParams() as { groupCode: string };
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);

  const handleCopyInviationCode = async () => {
    try {
      const res = await createInvitationCode(groupCode);
      const invitationCode = res.headers.get('location').split('/groups/in/')[1];
      const invitationLink = `
      링크를 클릭하거나, 참가 코드를 입력해주세요😀
      url: http://localhost:3000/invite/${invitationCode}
      코드: ${invitationCode}
      `;

      writeClipboard(invitationLink);
      alert('초대링크가 클립보드에 복사되었습니다💌');
    } catch (err) {
      alert(err);
    }
  };

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const res = await getGroups();
        setGroups(res);
        setIsLoading(false);
      } catch (err) {
        alert(err);
        setIsLoading(true);
      }
    };

    fetchGroups();
  }, []);

  if (isLoading) return <div>로딩중</div>;

  return (
    <StyledContainer>
      <StyledLogo src={Logo} alt={Logo} />
      <StyledGroupContainer>
        <StyledHeader>
          <StyledGroupHeaderButton type="button">Groups</StyledGroupHeaderButton>
        </StyledHeader>
        <StyledContent>
          {groups.map((group) => (
            <StyledGroupButton
              to={`groups/${group.code}`}
              isDefaultGroup={groupCode === group.code}
            >
              {group.name}
            </StyledGroupButton>
          ))}
        </StyledContent>
      </StyledGroupContainer>

      <StyledInvitationLink onClick={handleCopyInviationCode}>
        <img src={LinkIcon} alt="inivation-link" />
        <p>초대 링크 복사</p>
      </StyledInvitationLink>
    </StyledContainer>
  );
}

const StyledContainer = styled.div(
  ({ theme }) => `
  width: 36.4rem;
  height: 100vh;
  position: sticky;
  top: 0;
  border-right: 0.1rem solid ${theme.colors.GRAY_200};
  background: ${theme.colors.WHITE_100};
  padding: 4rem;
  gap: 2rem;
`
);

const StyledLogo = styled.img`
  width: 12rem;
`;

const StyledInvitationLink = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  position: absolute;
  bottom: 3.6rem;
  left: 3.6rem;
  gap: 1.2rem;
  font-size: 1.6rem;
`;

// TODO: 네이밍

const StyledGroupContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 2.8rem;
`;
const StyledHeader = styled.div``;

const StyledContent = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1.6rem;
`;

const StyledGroupHeaderButton = styled.button`
  width: 100%;
  font-size: 1.6rem;
  text-align: left;
`;

const StyledGroupButton = styled(Link)<{ isDefaultGroup: boolean }>(
  ({ theme, isDefaultGroup }) => `
  width: 100%;
  font-size: 1.6rem;
  color: ${isDefaultGroup ? theme.colors.BLACK_100 : theme.colors.GRAY_400};
  text-align: left;
  
`
);

export default Sidebar;
