import React, { InputHTMLAttributes } from 'react';
import styled from '@emotion/styled';
import Input from '../../common/Input/Input';
import { Appointment } from '../../../types/appointment';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  description: Appointment['description'];
}

function AppointmentCreateFormDescriptionInput({ description, onChange }: Props) {
  return (
    <>
      <StyledLabel>설명</StyledLabel>
      <Input
        placeholder="약속에 대한 설명을 입력해주세요"
        fontSize="3.2rem"
        textAlign="start"
        value={description}
        onChange={onChange}
        required
      />
    </>
  );
}

const StyledLabel = styled.label(
  ({ theme }) => `
  font-size: 4rem;
  color: ${theme.colors.PURPLE_100};
`
);

export default AppointmentCreateFormDescriptionInput;