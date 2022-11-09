import { ChangeEvent } from 'react';
import { useTheme } from '@emotion/react';
import Button from '@/components/Button/Button';
import Input from '@/components/Input/Input';
import TextField from '@/components/TextField/TextField';
import { StyledBinIcon } from '@/pages/RoleMainPage/components/RoleMainRoleEditModalInputs/RoleMainRoleEditModalInputs.styles';
import binImg from '@/assets/bin.svg';
import { EditRolesRequest } from '@/types/role';

type Props = {
  roles: EditRolesRequest['roles'];
  onChangeRoleInput: (targetIdx: number) => (e: ChangeEvent<HTMLInputElement>) => void;
  onClickDeleteButton: (targetIdx: number) => () => void;
  onClickAddButton: () => void;
};
function RoleMainRoleEditModalInputs({
  roles,
  onChangeRoleInput,
  onClickDeleteButton,
  onClickAddButton
}: Props) {
  const theme = useTheme();

  return (
    <>
      {roles.map((role, idx) => (
        <TextField
          key={idx}
          variant="filled"
          colorScheme={theme.colors.WHITE_100}
          borderRadius="1.2rem"
          padding="1.6rem 5rem"
          width="50.4rem"
        >
          <Input
            value={role}
            onChange={onChangeRoleInput(idx)}
            fontSize="1.6rem"
            required
            autoFocus
          />
          <StyledBinIcon src={binImg} alt="bin-icon" onClick={onClickDeleteButton(idx)} />
        </TextField>
      ))}
      <Button
        variant="filled"
        colorScheme={theme.colors.YELLOW_200}
        fontSize="2rem"
        width="50.4rem"
        onClick={onClickAddButton}
      >
        +
      </Button>
    </>
  );
}

export default RoleMainRoleEditModalInputs;
