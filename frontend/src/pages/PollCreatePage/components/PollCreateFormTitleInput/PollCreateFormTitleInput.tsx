import { useTheme } from '@emotion/react';
import { ChangeEventHandler, InputHTMLAttributes, memo } from 'react';
import Input from '../../../../components/Input/Input';
import TextField from '../../../../components/TextField/TextField';
import { PollInterface } from '../../../../types/poll';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  title: PollInterface['title'];
  onChange: ChangeEventHandler<HTMLInputElement>;
}

function PollCreateFormTitleInput({ title, onChange }: Props) {
  const theme = useTheme();

  return (
    <TextField variant="unstyled">
      <Input
        value={title}
        placeholder="투표 제목을 입력해주세요🧐"
        aria-label="poll-title"
        fontSize="3.2rem"
        color={theme.colors.BLACK_100}
        textAlign="left"
        required
        autoFocus
        onChange={onChange}
      />
    </TextField>
  );
}

export default memo(PollCreateFormTitleInput, (prev, next) => prev.title === next.title);
