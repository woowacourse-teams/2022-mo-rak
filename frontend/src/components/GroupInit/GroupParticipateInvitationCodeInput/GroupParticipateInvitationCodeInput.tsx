import { useTheme } from '@emotion/react';
import React, { ChangeEventHandler } from 'react';
import TextField from '../../@common/TextField/TextField';
import Input from '../../@common/Input/Input';

interface Props {
  invitationCode: string;
  handleInvitationCode: ChangeEventHandler<HTMLInputElement>;
}

function GroupParticipateInvitationCodeInput({ invitationCode, handleInvitationCode }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="outlined"
      colorScheme={theme.colors.PURPLE_100}
      borderRadius="10px"
      padding="1.2rem 0"
    >
      <Input
        placeholder="코드를 입력해주세요!"
        value={invitationCode}
        onChange={handleInvitationCode}
      />
    </TextField>
  );
}

export default GroupParticipateInvitationCodeInput;
