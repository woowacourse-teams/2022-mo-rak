import React, { ChangeEventHandler } from 'react';
import { useTheme } from '@emotion/react';
import Input from '../../@common/Input/Input';
import TextField from '../../@common/TextField/TextField';
import { GroupInterface } from '../../../types/group';

interface Props {
  groupName: GroupInterface['name'];
  onChange: ChangeEventHandler<HTMLInputElement>;
}

function GroupCreateFormNameInput({ groupName, onChange }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="outlined"
      colorScheme={theme.colors.PURPLE_100}
      borderRadius="10px"
      padding="1.2rem 0"
    >
      <Input
        placeholder="그룹이름을 입력해주세요!"
        value={groupName}
        onChange={onChange}
        autoFocus
      />
    </TextField>
  );
}

export default GroupCreateFormNameInput;
