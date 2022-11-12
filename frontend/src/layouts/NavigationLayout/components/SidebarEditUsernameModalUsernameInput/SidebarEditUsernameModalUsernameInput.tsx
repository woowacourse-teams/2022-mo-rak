import { ChangeEventHandler } from 'react';

import Input from '@/components/Input/Input';
import TextField from '@/components/TextField/TextField';

import { Member } from '@/types/group';
import { useTheme } from '@emotion/react';

type Props = {
  username: Member['name'];
  onChangeUsername: ChangeEventHandler<HTMLInputElement>;
};

function SidebarEditUsernameModalUsernameInput({ username, onChangeUsername }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="filled"
      colorScheme={theme.colors.WHITE_100}
      borderRadius="1.2rem"
      padding="1.6rem 5rem"
      width="70%"
    >
      <Input value={username} onChange={onChangeUsername} fontSize="1.6rem" autoFocus required />
    </TextField>
  );
}

export default SidebarEditUsernameModalUsernameInput;
