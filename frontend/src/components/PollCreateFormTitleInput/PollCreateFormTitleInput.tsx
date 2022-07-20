import { useTheme } from '@emotion/react';
import React, { InputHTMLAttributes } from 'react';
import Input from '../common/Input/Input';
import TextField from '../common/TextField/TextField';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  title: string;
}

function PollCreateFormTitleInput({ title, ...props }: Props) {
  const theme = useTheme();

  return (
    <TextField variant="unstyled" colorScheme={theme.colors.PURPLE_100}>
      <Input
        value={title}
        placeholder="투표 제목을 입력해주세요🧐"
        fontSize="3.2rem"
        color={theme.colors.BLACK_100}
        textAlign="left"
        required
        {...props}
      />
    </TextField>
  );
}

export default PollCreateFormTitleInput;
