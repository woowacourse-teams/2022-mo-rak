import { InputHTMLAttributes, memo } from 'react';
import Input from '../../../../components/Input/Input';
import { AppointmentInterface } from '../../../../types/appointment';
import { StyledLabel } from './AppointmentCreateFormTitleInput.style';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  title: AppointmentInterface['title'];
}

function AppointmentCreateFormTitleInput({ title, onChange }: Props) {
  return (
    <>
      <StyledLabel htmlFor="appointment-title">제목</StyledLabel>
      <Input
        id="appointment-title"
        placeholder="약속 제목을 입력해주세요"
        fontSize="2.4rem"
        textAlign="start"
        value={title}
        onChange={onChange}
        aria-label="appointment-title"
        required
        autoFocus
      />
    </>
  );
}

export default memo(AppointmentCreateFormTitleInput, (prev, next) => prev.title === next.title);
