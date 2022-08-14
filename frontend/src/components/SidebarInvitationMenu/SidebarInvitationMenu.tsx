import React from 'react';
import styled from '@emotion/styled';
import { GroupInterface } from '../../types/group';
import Plus from '../../assets/plus.svg';
import { createInvitationCode } from '../../api/group';
import { writeClipboard } from '../../utils/clipboard';

interface Props {
  groupCode: GroupInterface['code'];
}

function SidebarInvitationMenu({ groupCode }: Props) {
  const handleCopyInviationCode = async () => {
    try {
      if (groupCode) {
        const res = await createInvitationCode(groupCode);
        const invitationCode = res.headers.location.split('groups/in')[1];
        const invitationLink = `
        링크를 클릭하거나, 참가 코드를 입력해주세요😀
        url: ${process.env.CLIENT_URL}/invite/${invitationCode}}
        코드: ${invitationCode}
        `;

        writeClipboard(invitationLink);
        alert('초대링크가 클립보드에 복사되었습니다💌');
      }
    } catch (err) {
      alert(err);
    }
  };
  return (
    <StyledInvitationLink onClick={handleCopyInviationCode}>
      <img src={Plus} alt="inivation-link" />
      <StyledInviteText>새로운 멤버 초대</StyledInviteText>
    </StyledInvitationLink>
  );
}

const StyledInvitationLink = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
  padding: 0.4rem;
`;

const StyledInviteText = styled.p`
  font-size: 2rem;
`;

export default SidebarInvitationMenu;
