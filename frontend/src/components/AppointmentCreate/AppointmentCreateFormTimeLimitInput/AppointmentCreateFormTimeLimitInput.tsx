import styled from '@emotion/styled';
import React, { ChangeEventHandler } from 'react';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { Time } from '../../../types/appointment';
import AppointmentCreateFormTimeInput from '../AppointmentCreateFormTimeInput/AppointmentCreateFormTimeInput';

interface Props {
  startTime: Time;
  endTime: Time;
  onChangeStartTime: ChangeEventHandler<HTMLSelectElement>;
  onChangeEndTime: ChangeEventHandler<HTMLSelectElement>;
}

function AppointmentCreateFormTimeLimitInput({
  startTime,
  endTime,
  onChangeStartTime,
  onChangeEndTime
}: Props) {
  return (
    <>
      <StyledLabel>가능 시간 제한</StyledLabel>
      <StyledHelperText>AM 12:00 ~ AM 12:00(다음날)은 하루종일을 의미합니다.</StyledHelperText>
      <FlexContainer alignItems="center" gap="2.8rem">
        <AppointmentCreateFormTimeInput time={startTime} onChange={onChangeStartTime} />
        <StyledContent>~</StyledContent>
        <AppointmentCreateFormTimeInput time={endTime} onChange={onChangeEndTime} />
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

const StyledContent = styled.p`
  font-size: 3.2rem;
`;

const StyledHelperText = styled.p`
  font-size: 1.6rem;
`;

export default AppointmentCreateFormTimeLimitInput;
