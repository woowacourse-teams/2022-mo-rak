import { useTheme } from '@emotion/react';
import { memo, SelectHTMLAttributes } from 'react';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import TextField from '../../../../components/TextField/TextField';
import Select from '../../../../components/Select/Select';
import { createRange } from '../../../../utils/number';
import { Time } from '../../../../types/appointment';
import { StyledContent, StyledLabel } from './AppointmentCreateFormDurationInput.style';

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
        borderRadius="1.2rem"
        padding="0.4rem 0.8rem"
        width="6rem"
      >
        <Select
          id="hour"
          onChange={onChange}
          value={hour}
          name="hour"
          aria-label="appointment-duration-hour"
          required
        >
          <option value="">--</option>
          {createRange({
            size: 25
          }).map((hour: number) => (
            <option key={hour} value={hour}>
              {hour}
            </option>
          ))}
        </Select>
      </TextField>
      <StyledLabel htmlFor="hour">시간</StyledLabel>
      <TextField
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
        borderRadius="1.2rem"
        padding="0.4rem 0.8rem"
        width="9.2rem"
      >
        <Select
          id="minute"
          onChange={onChange}
          value={minute}
          name="minute"
          fontSize="2.4rem"
          aria-label="appointment-duration-minute"
          required
        >
          <option value="00">00</option>
          <option value="30">30</option>
        </Select>
      </TextField>
      <StyledLabel htmlFor="minute">분</StyledLabel>
      <StyledContent>동안 진행</StyledContent>
    </FlexContainer>
  );
}

export default memo(
  AppointmentCreateFormDurationInput,
  (prev, next) => prev.duration === next.duration
);
