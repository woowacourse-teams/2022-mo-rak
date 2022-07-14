import { useTheme } from '@emotion/react';
import React, { InputHTMLAttributes, ChangeEventHandler } from 'react';
import Input from '../common/Input/Input';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  title: string;
  onChange: ChangeEventHandler<HTMLInputElement>;
}

function PollCreateFormTitleInput({ title, onChange }: Props) {
  const theme = useTheme();

  return (
    <Input
      value={title}
      colorScheme={theme.colors.PURPLE_100}
      variant="unstyled"
      placeholder="투표 제목을 입력해주세요🧐"
      color={theme.colors.BLACK_100}
      fontSize="3.2rem"
      textAlign="left"
      onChange={onChange}
      required
    />
  );
}

export default PollCreateFormTitleInput;
