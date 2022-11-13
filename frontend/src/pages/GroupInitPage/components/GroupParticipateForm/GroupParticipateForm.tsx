import { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';

import { participateGroup } from '@/api/group';
import useInput from '@/hooks/useInput';

import GroupParticipateInvitationCodeInput from '@/pages/GroupInitPage/components/GroupParticipateInvitationCodeInput/GroupParticipateInvitationCodeInput';
import GroupParticipateFormSubmitButton from '@/pages/GroupInitPage/components/GroupParticipateFormSubmitButton/GroupParticipateFormSubmitButton';
import {
  StyledContainer,
  StyledInputContainer
} from '@/pages/GroupInitPage/components/GroupParticipateForm/GroupParticipateForm.styles';
import { AxiosError } from 'axios';
import { GROUP_ERROR } from '@/constants/errorMessage';

function GroupParticipateForm() {
  const [invitationCode, handleInvitationCode, resetInvitationCode] = useInput('');

  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!invitationCode) {
      alert(GROUP_ERROR.EMPTY_INVITATION_CODE);

      return;
    }

    try {
      const res = await participateGroup(invitationCode);
      const groupCode = res.headers.location.split('/groups/')[1];
      navigate(`/groups/${groupCode}`);
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '1301') {
          alert(GROUP_ERROR.INVALID_INVITATION_CODE);
          resetInvitationCode();
        }
      }
    }
  };

  return (
    <StyledContainer onSubmit={handleSubmit}>
      <StyledInputContainer>
        <GroupParticipateInvitationCodeInput
          invitationCode={invitationCode}
          handleInvitationCode={handleInvitationCode}
        />
        <GroupParticipateFormSubmitButton />
      </StyledInputContainer>
    </StyledContainer>
  );
}

export default GroupParticipateForm;
