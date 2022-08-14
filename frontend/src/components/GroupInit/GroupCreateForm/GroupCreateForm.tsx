import styled from '@emotion/styled';
import React, { ChangeEvent, FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createGroup } from '../../../api/group';
import { GroupInterface } from '../../../types/group';
import GroupCreateFormNameInput from '../GroupCreateFormNameInput/GroupCreateFormNameInput';
import GroupCreateFormSubmitButton from '../GroupCreateFormSubmitButton/GroupCreateFormSubmitButton';

function GroupCreateForm() {
  const navigate = useNavigate();
  const [groupName, setGroupName] = useState<GroupInterface['name']>('');

  const handleCreateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await createGroup(groupName);
      const groupCode = res.headers.location.split('groups/')[1];

      navigate(`/groups/${groupCode}`);
    } catch (err) {
      alert(err);
    }
  };

  const handleGroupName = (e: ChangeEvent<HTMLInputElement>) => {
    setGroupName(e.target.value);
  };

  return (
    <StyledForm onSubmit={handleCreateGroup}>
      <StyledTitle>그룹생성</StyledTitle>
      <GroupCreateFormNameInput groupName={groupName} onChange={handleGroupName} />
      <GroupCreateFormSubmitButton />
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

export default GroupCreateForm;
