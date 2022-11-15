import { ChangeEvent, FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createGroup } from '@/apis/group';
import { Group } from '@/types/group';
import GroupInitFormsCreateFormNameInput from '@/pages/GroupInitPage/components/GroupInitFormsCreateFormNameInput/GroupInitFormsCreateFormNameInput';
import GroupInitFormsCreateFormSubmitButton from '@/pages/GroupInitPage/components/GroupInitFormsCreateFormSubmitButton/GroupInitFormsCreateFormSubmitButton';
import {
  StyledContainer,
  StyledInputContainer
} from '@/pages/GroupInitPage/components/GroupInitFormsCreateForm/GroupInitFormsCreateForm.styles';
import { AxiosError } from 'axios';

function GroupInitFormsCreateForm() {
  const navigate = useNavigate();
  const [groupName, setGroupName] = useState<Group['name']>('');

  const handleCreateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await createGroup(groupName);
      const groupCode = res.headers.location.split('groups/')[1];

      navigate(`/groups/${groupCode}`);
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '4000') {
          alert('그룹이름을 입력해주세요!');
        }
      }
    }
  };

  const handleGroupName = (e: ChangeEvent<HTMLInputElement>) => {
    setGroupName(e.target.value);
  };

  return (
    <StyledContainer onSubmit={handleCreateGroup}>
      <StyledInputContainer>
        <GroupInitFormsCreateFormNameInput groupName={groupName} onChange={handleGroupName} />
        <GroupInitFormsCreateFormSubmitButton />
      </StyledInputContainer>
    </StyledContainer>
  );
}

export default GroupInitFormsCreateForm;
