import { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';

import { participateGroup } from '@/apis/group';
import useInput from '@/hooks/useInput';

import GroupInitFormsParticipateFormInvitationCodeInput from '@/pages/GroupInitPage/components/GroupInitFormsParticipateFormInvitationCodeInput/GroupInitFormsParticipateFormInvitationCodeInput';
import GroupInitFormsParticipateFormSubmitButton from '@/pages/GroupInitPage/components/GroupInitFormsParticipateFormSubmitButton/GroupInitFormsParticipateFormSubmitButton';
import {
  StyledContainer,
  StyledInputContainer
} from '@/pages/GroupInitPage/components/GroupInitFormsParticipateForm/GroupInitFormsParticipateForm.styles';
import { AxiosError } from 'axios';

function GroupInitFormsParticipateForm() {
  const [invitationCode, handleInvitationCode, resetInvitationCode] = useInput('');

  const navigate = useNavigate();

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!invitationCode) {
      alert('그룹 코드를 입력해주세요!');

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
          alert('그룹 코드를 다시 한 번 확인해주세요!');
          resetInvitationCode();
        }
      }
    }
  };

  return (
    <StyledContainer onSubmit={handleSubmit}>
      <StyledInputContainer>
        <GroupInitFormsParticipateFormInvitationCodeInput
          invitationCode={invitationCode}
          handleInvitationCode={handleInvitationCode}
        />
        <GroupInitFormsParticipateFormSubmitButton />
      </StyledInputContainer>
    </StyledContainer>
  );
}

export default GroupInitFormsParticipateForm;
