import styled from '@emotion/styled';
import React, { ChangeEvent, FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createGroup } from '../../../api/group';
import theme from '../../../styles/theme';
import Button from '../../common/Button/Button';

import Input from '../../common/Input/Input';
import TextField from '../../common/TextField/TextField';

function GroupCreateForm() {
  const navigate = useNavigate();
  const [groupName, setGroupName] = useState('');

  const handleCreateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await createGroup(groupName);
      const groupCode = res.headers.get('location').split('groups/')[1];

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
      <TextField
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
        borderRadius="10px"
        padding="1.2rem 0"
      >
        {/* TODO: Input 내부 레이아웃 수정 */}
        <Input
          placeholder="그룹이름을 입력해주세요!"
          value={groupName}
          onChange={handleGroupName}
        />
      </TextField>
      <Button
        type="submit"
        variant="filled"
        colorScheme={theme.colors.PURPLE_100}
        color={theme.colors.WHITE_100}
        fontSize="1.6rem"
      >
        생성하기
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

export default GroupCreateForm;
