import { InputHTMLAttributes, memo } from 'react';
import styled from '@emotion/styled';
import Input from '../../../../components/Input/Input';
import { AppointmentInterface } from '../../../../types/appointment';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  description: AppointmentInterface['description'];
}

function AppointmentCreateFormDescriptionInput({ description, onChange }: Props) {
  return (
    <>
      <StyledLabel htmlFor="appointment-description">설명</StyledLabel>
      <Input
        id="appointment-description"
        placeholder="약속에 대한 설명을 입력해주세요"
        fontSize="2.4rem"
        textAlign="start"
        value={description}
        onChange={onChange}
        aria-label="appointment-description"
        required
      />
    </>
  );
}

const StyledLabel = styled.label(
  ({ theme }) => `
  font-size: 2.8rem;
  color: ${theme.colors.PURPLE_100};
`
);

export default memo(
  AppointmentCreateFormDescriptionInput,
  (prev, next) => prev.description === next.description
);
