import { useTheme } from '@emotion/react';
import React, { InputHTMLAttributes } from 'react';
import Input from '../common/Input/Input';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  pollTitle: string;
  handlePollTitle: React.ChangeEventHandler<HTMLInputElement>;
}

function PollCreateFormTitle({ pollTitle, handlePollTitle }: Props) {
  const theme = useTheme();

  return (
    <Input
      value={pollTitle}
      colorScheme={theme.colors.PURPLE_100}
      variant="unstyled"
      placeholder="투표 제목을 입력해주세요🧐"
      color={theme.colors.BLACK_100}
      fontSize="3.2rem"
      textAlign="left"
      onChange={handlePollTitle}
      required
    />
  );
}

export default PollCreateFormTitle;
