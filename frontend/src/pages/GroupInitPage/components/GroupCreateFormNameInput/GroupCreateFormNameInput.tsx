import { ChangeEventHandler } from 'react';

import { useTheme } from '@emotion/react';
import Input from '../../../../components/Input/Input';
import TextField from '../../../../components/TextField/TextField';
import { GroupInterface } from '../../../../types/group';
import Create from '../../../../assets/create-plus.svg';
import { StyledCreateIcon } from './GroupCreateFormNameInput.styles';

interface Props {
  groupName: GroupInterface['name'];
  onChange: ChangeEventHandler<HTMLInputElement>;
}

function GroupCreateFormNameInput({ groupName, onChange }: Props) {
  const theme = useTheme();

  return (
    <TextField
      width="60vw"
      variant="filled"
      colorScheme={theme.colors.WHITE_100}
      borderRadius="10px 0 0 10px"
      padding="2.8rem 8rem"
    >
      <StyledCreateIcon src={Create} alt="create-group-icon" />
      <Input
        placeholder="그룹이름을 입력해주세요!"
        value={groupName}
        onChange={onChange}
        fontSize="2.4rem"
        autoFocus
      />
    </TextField>
  );
}

export default GroupCreateFormNameInput;
