import React, { ChangeEventHandler } from 'react';
import styled from '@emotion/styled';
import Input from '../../common/Input/Input';
import FlexContainer from '../../common/FlexContainer/FlexContainer';

interface Props {
  closeTime: string;
  onChangeTime: ChangeEventHandler<HTMLInputElement>;
  closeDate: string;
  onChangeDate: ChangeEventHandler<HTMLInputElement>;
}

function AppointmentCreateFormCloseTimeInput({
  closeTime,
  onChangeTime,
  closeDate,
  onChangeDate
}: Props) {
  return (
    <>
      <StyledLabel>마감 시간 설정</StyledLabel>
      <FlexContainer alignItems="center" gap="2.8rem">
        <Input
          type="date"
          min={new Date().toISOString().split('T')[0]}
          onChange={onChangeDate}
          value={closeDate}
        />
        <Input type="time" onChange={onChangeTime} value={closeTime} />
      </FlexContainer>
    </>
  );
}

const StyledLabel = styled.label(
  ({ theme }) => `
  font-size: 4rem;
  color: ${theme.colors.PURPLE_100};
`
);

export default AppointmentCreateFormCloseTimeInput;
