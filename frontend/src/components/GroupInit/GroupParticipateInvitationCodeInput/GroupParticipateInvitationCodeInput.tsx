import { useTheme } from '@emotion/react';
import React, { ChangeEventHandler } from 'react';
import styled from '@emotion/styled';
import TextField from '../../@common/TextField/TextField';
import Input from '../../@common/Input/Input';
import Participate from '../../../assets/participate.svg';

interface Props {
  invitationCode: string;
  handleInvitationCode: ChangeEventHandler<HTMLInputElement>;
}

function GroupParticipateInvitationCodeInput({ invitationCode, handleInvitationCode }: Props) {
  const theme = useTheme();

  return (
    <TextField
      width="83.2rem"
      variant="filled"
      colorScheme={theme.colors.WHITE_100}
      borderRadius="10px 0 0 10px"
      padding="2.8rem 8rem"
    >
      <StyledParticipateIcon src={Participate} alt="participate-group-icon" />
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

const StyledParticipateIcon = styled.img`
  position: absolute;
  top: 2rem;
  left: 2.4rem;
`;

export default GroupParticipateInvitationCodeInput;
