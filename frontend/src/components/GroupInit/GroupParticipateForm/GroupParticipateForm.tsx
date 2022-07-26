import styled from '@emotion/styled';
import React, { ChangeEvent, FormEvent, useState } from 'react';
import { participateGroup } from '../../../api/group';
import theme from '../../../styles/theme';

import Button from '../../common/Button/Button';
import Input from '../../common/Input/Input';
import TextField from '../../common/TextField/TextField';

function GroupParticipateForm() {
  const [invitationCode, setInvitationCode] = useState('');

  const handleInvitationCode = (e: ChangeEvent<HTMLInputElement>) => {
    setInvitationCode(e.target.value);
    console.log(invitationCode);
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await participateGroup(invitationCode);
      console.log(res);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <StyledForm onSubmit={handleSubmit}>
      <StyledTitle>그룹참가</StyledTitle>
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
      <Button
        variant="filled"
        colorScheme={theme.colors.PURPLE_100}
        color={theme.colors.WHITE_100}
        fontSize="1.6rem"
        type="submit"
      >
        참가하기
      </Button>
    </StyledForm>
  );
}

const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1.2rem;
`;

const StyledTitle = styled.span`
  font-size: 2rem;
`;

export default GroupParticipateForm;
