import React, { ChangeEventHandler } from 'react';
import styled from '@emotion/styled';

import { useTheme } from '@emotion/react';
import Input from '../../@common/Input/Input';
import TextField from '../../@common/TextField/TextField';
import { GroupInterface } from '../../../types/group';
import Participate from '../../../assets/participate.svg';

interface Props {
  groupName: GroupInterface['name'];
  onChange: ChangeEventHandler<HTMLInputElement>;
}

function GroupCreateFormNameInput({ groupName, onChange }: Props) {
  const theme = useTheme();

  return (
    <TextField
      width="83.2rem"
      variant="filled"
      colorScheme={theme.colors.WHITE_100}
      borderRadius="10px 0 0 10px"
      padding="2.8rem 8rem"
    >
      <StyledParticipateIcon src={Participate} alt="participate-group-icon" />
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

const StyledParticipateIcon = styled.img`
  position: absolute;
  top: 2rem;
  left: 2.4rem;
`;

export default GroupCreateFormNameInput;
