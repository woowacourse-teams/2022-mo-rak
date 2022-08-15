import React, { InputHTMLAttributes } from 'react';
import styled from '@emotion/styled';
import Input from '../../@common/Input/Input';
import { AppointmentInterface } from '../../../types/appointment';

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
        fontSize="3.2rem"
        textAlign="start"
        value={title}
        onChange={onChange}
        required
        autoFocus
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

export default AppointmentCreateFormTitleInput;
