import styled from '@emotion/styled';
import React, { ChangeEvent, FormEvent, useState } from 'react';
import { createGroup } from '../../api/group';
import theme from '../../styles/theme';
import Box from '../common/Box/Box';
import Button from '../common/Button/Button';

import Input from '../common/Input/Input';
import TextField from '../common/TextField/TextField';

function GroupCreateForm() {
  const [groupName, setGroupName] = useState('');

  const handleCreateGroup = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const res = await createGroup(groupName);
      const location = res.headers.get('location').split('groups/')[1];
    } catch (err) {
      alert(err);
    }
  };

  const handleGroupName = (e: ChangeEvent<HTMLInputElement>) => {
    setGroupName(e.target.value);
  };

  return (
    <Box width="60rem" height="21.6rem">
      <StyledForm onSubmit={handleCreateGroup}>
        <StyledTitle>그룹생성</StyledTitle>
        <TextField
          variant="outlined"
          width="52rem"
          height="3.6rem"
          colorScheme={theme.colors.PURPLE_100}
          borderRadius="10px"
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
          width="52rem"
          colorScheme={theme.colors.PURPLE_100}
          color={theme.colors.WHITE_100}
          fontSize="1.6rem"
        >
          생성하기
        </Button>
      </StyledForm>
    </Box>
  );
}

const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1.2rem;
  height: 100%;
`;

const StyledTitle = styled.span`
  font-size: 2rem;
`;

export default GroupCreateForm;
