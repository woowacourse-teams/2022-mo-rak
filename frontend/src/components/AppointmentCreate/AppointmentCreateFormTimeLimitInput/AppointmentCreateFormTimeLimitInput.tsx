import styled from '@emotion/styled';
import { ChangeEventHandler } from 'react';
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
      <StyledTitle>가능 시간 제한 설정</StyledTitle>
      <StyledHelperText>AM 12:00 ~ AM 12:00(다음날)은 하루종일을 의미합니다.</StyledHelperText>
      <FlexContainer alignItems="center" gap="2.8rem">
        <AppointmentCreateFormTimeInput time={startTime} onChange={onChangeStartTime} />
        <StyledContent>~</StyledContent>
        <AppointmentCreateFormTimeInput time={endTime} onChange={onChangeEndTime} />
      </FlexContainer>
    </>
  );
}

const StyledTitle = styled.div(
  ({ theme }) => `
  font-size: 2.8rem;
  color: ${theme.colors.PURPLE_100};
`
);

const StyledContent = styled.p`
  font-size: 2.4rem;
`;

const StyledHelperText = styled.p`
  font-size: 2rem;
`;

export default AppointmentCreateFormTimeLimitInput;
