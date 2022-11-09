import { ChangeEventHandler, memo } from 'react';
import { useTheme } from '@emotion/react';
import TextField from '@/components/TextField/TextField';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import Select from '@/components/Select/Select';
import { createRange } from '@/utils/createRange';
import { Time } from '@/types/appointment';
import { StyledContent } from '@/pages/AppointmentCreatePage/components/AppointmentCreateFormTimeInput/AppointmentCreateFormTimeInput.styles';

type Props = {
  time: Time;
  ariaLabelHelper: 'start' | 'end';
  onChange: ChangeEventHandler<HTMLSelectElement>;
};

function AppointmentCreateFormTimeInput({ time, ariaLabelHelper, onChange }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="outlined"
      colorScheme={theme.colors.PURPLE_100}
      borderRadius="1.2rem"
      padding="0.4rem 0.8rem"
      width="18rem"
    >
      <FlexContainer alignItems="center">
        {/* TODO: label을 어떻게 넣을것인가? 빈값으로? */}
        <Select
          name="period"
          value={time.period}
          onChange={onChange}
          fontSize="2.2rem"
          aria-label={`appointment-${ariaLabelHelper}-time-limit-period`}
          required
        >
          <option value="AM">오전</option>
          <option value="PM">오후</option>
        </Select>
        <Select
          name="hour"
          value={time.hour}
          onChange={onChange}
          fontSize="2.2rem"
          aria-label={`appointment-${ariaLabelHelper}-time-limit-hour`}
          required
        >
          <option value="">--</option>
          {createRange({
            size: 12,
            startNumber: 1
          }).map((hour: number) => (
            <option key={hour} value={hour}>
              {hour}
            </option>
          ))}
        </Select>
        <StyledContent>:</StyledContent>
        <Select
          name="minute"
          value={time.minute}
          onChange={onChange}
          fontSize="2.2rem"
          aria-label={`appointment-${ariaLabelHelper}-time-limit-minute`}
          required
        >
          <option value="00">00</option>
          <option value="30">30</option>
        </Select>
      </FlexContainer>
    </TextField>
  );
}

export default memo(AppointmentCreateFormTimeInput, (prev, next) => prev.time === next.time);
