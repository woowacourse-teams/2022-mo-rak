import { Group } from '@/types/group';
import plusImg from '@/assets/plus.svg';
import { createInvitationCode } from '@/api/group';
import { writeClipboard } from '../../../../utils/clipboard';
import {
  StyledInvitationLink,
  StyledInvitationText,
  StyledPlusIcon
} from './SidebarInvitationMenu.styles';

type Props = {
  groupCode: Group['code'];
};

function SidebarInvitationMenu({ groupCode }: Props) {
  const handleCopyInvitationCode = async () => {
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
  };

  return (
    <StyledInvitationLink onClick={handleCopyInvitationCode}>
      <StyledPlusIcon src={plusImg} alt="invitation-link" />
      <StyledInvitationText>새로운 멤버 초대</StyledInvitationText>
    </StyledInvitationLink>
  );
}

export default SidebarInvitationMenu;
