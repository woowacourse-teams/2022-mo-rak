import { FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';

import { participateGroup } from '../../../../api/group';
import useInput from '../../../../hooks/useInput';

import GroupParticipateInvitationCodeInput from '../GroupParticipateInvitationCodeInput/GroupParticipateInvitationCodeInput';
import GroupParticipateFormSubmitButton from '../GroupPariticipateFormSubmitButton/GroupParticipateFormSubmitButton';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { StyledForm } from './GroupParticipateForm.style';
import { AxiosError } from 'axios';

function GroupParticipateForm() {
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
    <StyledForm onSubmit={handleSubmit}>
      <FlexContainer>
        <GroupParticipateInvitationCodeInput
          invitationCode={invitationCode}
          handleInvitationCode={handleInvitationCode}
        />
        <GroupParticipateFormSubmitButton />
      </FlexContainer>
    </StyledForm>
  );
}

export default GroupParticipateForm;
