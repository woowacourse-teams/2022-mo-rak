import { Group } from '@/types/group';
import plusImg from '@/assets/plus.svg';
import { createInvitationCode } from '@/api/group';
import { writeClipboard } from '@/utils/clipboard';
import {
  StyledContainer,
  StyledInvitationText,
  StyledPlusIcon
} from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerBottomSectionInvitationMenu/GlobalFootbarFootbarDrawerBottomSectionInvitationMenu.styles';
import { SUCCESS_MESSAGE } from '@/constants/message';

type Props = {
  groupCode: Group['code'];
};

function GlobalFootbarFootbarDrawerBottomSectionInvitationMenu({ groupCode }: Props) {
  const handleCopyInvitationCode = async () => {
    const res = await createInvitationCode(groupCode);
    const invitationCode = res.headers.location.split('groups/in/')[1];
    const invitationLink = `
        링크를 클릭하거나, 참가 코드를 입력해주세요😀
        url: ${process.env.CLIENT_URL}/invite/${invitationCode}
        코드: ${invitationCode}
    `;

    writeClipboard(invitationLink).then(() => {
      alert(SUCCESS_MESSAGE.COPY_INVITATION_LINK);
    });
  };

  return (
    <StyledContainer onClick={handleCopyInvitationCode}>
      <StyledPlusIcon src={plusImg} alt="invitation-link" />
      <StyledInvitationText>새로운 멤버 초대</StyledInvitationText>
    </StyledContainer>
  );
}

export default GlobalFootbarFootbarDrawerBottomSectionInvitationMenu;
