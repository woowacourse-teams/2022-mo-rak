import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import React, { ChangeEventHandler } from 'react';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import TextField from '../../common/TextField/TextField';
import { createRange } from '../../../utils/number';
import Select from '../../common/Select/Select';
import { Time } from '../../../types/appointment';

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
  const theme = useTheme();

  return (
    <>
      <StyledLabel>가능 시간 제한</StyledLabel>
      <StyledHelperText>AM 12:00 ~ AM 12:00(다음날)은 하루종일을 의미합니다.</StyledHelperText>
      <FlexContainer alignItems="center" gap="2.8rem">
        <TextField
          variant="outlined"
          colorScheme={theme.colors.PURPLE_100}
          borderRadius="10px"
          padding="0.4rem 0.8rem"
          width="21.6rem"
        >
          <FlexContainer alignItems="center">
            {/* TODO: label을 어떻게 넣을것인가? 빈값으로? */}
            <Select name="period" value={startTime.period} onChange={onChangeStartTime} required>
              <option value="AM">AM</option>
              <option value="PM">PM</option>
            </Select>
            <Select name="hour" value={startTime.hour} onChange={onChangeStartTime} required>
              <option value="">--</option>
              {createRange({
                size: 12,
                startNumber: 1
              }).map((hour: number) => (
                <option value={hour}>{hour}</option>
              ))}
            </Select>
            <StyledContent>:</StyledContent>
            <Select name="minute" value={startTime.minute} onChange={onChangeStartTime} required>
              <option value="00">00</option>
              <option value="30">30</option>
            </Select>
          </FlexContainer>
        </TextField>
        <StyledContent>~</StyledContent>
        <TextField
          variant="outlined"
          colorScheme={theme.colors.PURPLE_100}
          borderRadius="10px"
          padding="0.4rem 0.8rem"
          width="21.6rem"
        >
          <FlexContainer alignItems="center">
            <Select name="period" value={endTime.period} onChange={onChangeEndTime} required>
              <option value="AM">AM</option>
              <option value="PM">PM</option>
            </Select>
            <Select name="hour" value={endTime.hour} onChange={onChangeEndTime} required>
              <option value="">--</option>
              {createRange({
                size: 12,
                startNumber: 1
              }).map((hour: number) => (
                <option value={hour}>{hour}</option>
              ))}
            </Select>
            <StyledContent>:</StyledContent>
            <Select name="minute" value={endTime.minute} onChange={onChangeEndTime} required>
              <option value="00">00</option>
              <option value="30">30</option>
            </Select>
          </FlexContainer>
        </TextField>
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

const StyledHelperText = styled.p`
  font-size: 1.6rem;
`;

const StyledContent = styled.p`
  font-size: 3.2rem;
`;

export default AppointmentCreateFormTimeLimitInput;
