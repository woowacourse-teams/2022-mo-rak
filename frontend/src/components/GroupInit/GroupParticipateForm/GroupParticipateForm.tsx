import styled from '@emotion/styled';
import React, { FormEvent } from 'react';
import { participateGroup } from '../../../api/group';
import useInput from '../../../hooks/useInput';

import GroupParticipateInvitationCodeInput from '../GroupParticipateInvitationCodeInput/GroupParticipateInvitationCodeInput';
import GroupParticipateFormSubmitButton from '../GroupPariticipateFormSubmitButton/GroupParticipateFormSubmitButton';

function GroupParticipateForm() {
  const [invitationCode, handleInvitationCode] = useInput('');

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
      <GroupParticipateInvitationCodeInput
        invitationCode={invitationCode}
        handleInvitationCode={handleInvitationCode}
      />
      <GroupParticipateFormSubmitButton />
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
