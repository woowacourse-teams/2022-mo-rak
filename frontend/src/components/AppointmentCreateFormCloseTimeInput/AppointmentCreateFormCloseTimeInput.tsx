import React from 'react';
import styled from '@emotion/styled';
import Input from '../common/Input/Input';
import FlexContainer from '../common/FlexContainer/FlexContainer';

function AppointmentCreateFormCloseTimeInput() {
  return (
    <>
      <StyledLabel>마감 시간 설정</StyledLabel>
      <FlexContainer alignItems="center" gap="2.8rem">
        <Input type="date" min={new Date().toISOString().split('T')[0]} />
        <Input type="time" />
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

// function AppointmentCreateFormTimeLimitInput({
//   startTime,
//   endTime,
//   onChangeStartTime,
//   onChangeEndTime
// }: Props) {
//   return (
//     <>
//       <StyledLabel>가능 시간 제한</StyledLabel>
//       <StyledHelperText>AM 12:00 ~ AM 12:00(다음날)은 하루종일을 의미합니다.</StyledHelperText>
//       <FlexContainer alignItems="center" gap="2.8rem">
//         <AppointmentCreateFormTimeInput time={startTime} onChange={onChangeStartTime} />
//         <StyledContent>~</StyledContent>
//         <AppointmentCreateFormTimeInput time={endTime} onChange={onChangeEndTime} />
//       </FlexContainer>
//     </>
//   );
// }

// const StyledContent = styled.p`
//   font-size: 3.2rem;
// `;

// const StyledHelperText = styled.p`
//   font-size: 1.6rem;
// `;
