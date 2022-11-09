import { useTheme } from '@emotion/react';
import { ChangeEventHandler } from 'react';
import TextField from '@/components/TextField/TextField';
import Input from '@/components/Input/Input';
import participateImg from '@/assets/participate.svg';
import { StyledParticipateIcon } from '@/pages/GroupInitPage/components/GroupParticipateInvitationCodeInput/GroupParticipateInvitationCodeInput.styles';

type Props = {
  invitationCode: string;
  handleInvitationCode: ChangeEventHandler<HTMLInputElement>;
};

function GroupParticipateInvitationCodeInput({ invitationCode, handleInvitationCode }: Props) {
  const theme = useTheme();

  return (
    <TextField
      width="75%"
      maxWidth="83.2rem"
      variant="filled"
      colorScheme={theme.colors.WHITE_100}
      borderRadius="10px 0 0 10px"
      padding="2.8rem 8rem"
    >
      <StyledParticipateIcon src={participateImg} alt="그룹 참가 아이콘" aria-hidden="true" />
      <Input
        placeholder="코드를 입력해주세요!"
        value={invitationCode}
        onChange={handleInvitationCode}
        fontSize="2.4rem"
        autoFocus
      />
    </TextField>
  );
}

export default GroupParticipateInvitationCodeInput;
