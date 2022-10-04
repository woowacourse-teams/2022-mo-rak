import styled from '@emotion/styled';
import { GroupInterface } from '../../../../types/group';
import Plus from '../../../../assets/plus.svg';
import { createInvitationCode } from '../../../../api/group';
import { writeClipboard } from '../../../../utils/clipboard';

interface Props {
  groupCode: GroupInterface['code'];
}

function SidebarInvitationMenu({ groupCode }: Props) {
  const handleCopyInvitationCode = async () => {
    try {
      const res = await createInvitationCode(groupCode);
      const invitationCode = res.headers.location.split('groups/in/')[1];
      const invitationLink = `
        링크를 클릭하거나, 참가 코드를 입력해주세요😀
        url: ${process.env.CLIENT_URL}/invite/${invitationCode}
        코드: ${invitationCode}
        `;

      writeClipboard(invitationLink).then(() => {
        alert('초대링크가 클립보드에 복사되었습니다💌');
      });
    } catch (err) {
      alert(err);
    }
  };
  return (
    <StyledInvitationLink onClick={handleCopyInvitationCode}>
      <StyledPlusIcon src={Plus} alt="invitation-link" />
      <StyledInvitationText>새로운 멤버 초대</StyledInvitationText>
    </StyledInvitationLink>
  );
}

const StyledInvitationLink = styled.button`
  display: flex;
  align-items: center;
  gap: 1.2rem;
  font-size: 1.6rem;
  padding: 0.4rem;
`;

const StyledInvitationText = styled.p`
  font-size: 1.6rem;
`;

const StyledPlusIcon = styled.img`
  width: 2.4rem;
`;

export default SidebarInvitationMenu;
