import { ChangeEvent, FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createGroup } from '../../../../api/group';
import { Group } from '../../../../types/group';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import GroupCreateFormNameInput from '../GroupCreateFormNameInput/GroupCreateFormNameInput';
import GroupCreateFormSubmitButton from '../GroupCreateFormSubmitButton/GroupCreateFormSubmitButton';
import { StyledForm } from './GroupCreateForm.styles';
import { AxiosError } from 'axios';

function GroupCreateForm() {
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
    <StyledForm onSubmit={handleCreateGroup}>
      <FlexContainer>
        <GroupCreateFormNameInput groupName={groupName} onChange={handleGroupName} />
        <GroupCreateFormSubmitButton />
      </FlexContainer>
    </StyledForm>
  );
}

export default GroupCreateForm;
