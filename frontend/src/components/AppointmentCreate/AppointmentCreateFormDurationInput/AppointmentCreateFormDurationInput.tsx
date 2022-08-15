import { useTheme } from '@emotion/react';
import React, { SelectHTMLAttributes } from 'react';
import styled from '@emotion/styled';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import TextField from '../../@common/TextField/TextField';
import Select from '../../@common/Select/Select';
import { createRange } from '../../../utils/number';
import { Time } from '../../../types/appointment';

interface Props extends SelectHTMLAttributes<HTMLSelectElement> {
  duration: Omit<Time, 'period'>;
}

function AppointmentCreateFormDurationInput({ duration, onChange }: Props) {
  const theme = useTheme();
  const { hour, minute } = duration;

  return (
    <FlexContainer alignItems="end" gap="1.2rem">
      <TextField
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
        borderRadius="10px"
        padding="0.4rem 0.8rem"
        width="6rem"
      >
        <Select id="hour" onChange={onChange} value={hour} name="hour" required>
          <option value="">--</option>
          {createRange({
            size: 25
          }).map((hour: number) => (
            <option value={hour}>{hour}</option>
          ))}
        </Select>
      </TextField>
      <StyledLabel htmlFor="hour">시간</StyledLabel>
      <TextField
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
        borderRadius="10px"
        padding="0.4rem 0.8rem"
        width="9.2rem"
      >
        <Select id="minute" onChange={onChange} value={minute} name="minute" required>
          <option value="00">00</option>
          <option value="30">30</option>
        </Select>
      </TextField>
      <StyledLabel htmlFor="minute">분</StyledLabel>
      <StyledContent>동안 진행</StyledContent>
    </FlexContainer>
  );
}

const StyledContent = styled.p`
  font-size: 3.2rem;
`;

const StyledLabel = styled.label`
  font-size: 3.2rem;
`;

export default AppointmentCreateFormDurationInput;
