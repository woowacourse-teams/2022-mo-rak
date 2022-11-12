import { InputHTMLAttributes, memo } from 'react';

import { StyledLabel } from '@/pages/AppointmentCreatePage/components/AppointmentCreateFormDescriptionInput/AppointmentCreateFormDescriptionInput.styles';

import Input from '@/components/Input/Input';

import { Appointment } from '@/types/appointment';

type Props = {
  description: Appointment['description'];
} & InputHTMLAttributes<HTMLInputElement>;

function AppointmentCreateFormDescriptionInput({ description, onChange }: Props) {
  return (
    <>
      <StyledLabel htmlFor="appointment-description">설명</StyledLabel>
      <Input
        id="appointment-description"
        placeholder="약속에 대한 설명을 입력해주세요"
        fontSize="2.2rem"
        textAlign="start"
        value={description}
        onChange={onChange}
        aria-label="appointment-description"
        required
      />
    </>
  );
}

export default memo(
  AppointmentCreateFormDescriptionInput,
  (prev, next) => prev.description === next.description
);
