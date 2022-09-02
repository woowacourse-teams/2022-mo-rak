import { ChangeEventHandler } from 'react';
import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';
import TextField from '../../@common/TextField/TextField';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Select from '../../@common/Select/Select';
import { createRange } from '../../../utils/number';
import { Time } from '../../../types/appointment';

interface Props {
  time: Time;
  onChange: ChangeEventHandler<HTMLSelectElement>;
}

function AppointmentCreateFormTimeInput({ time, onChange }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="outlined"
      colorScheme={theme.colors.PURPLE_100}
      borderRadius="10px"
      padding="0.4rem 0.8rem"
      width="21.6rem"
    >
      <FlexContainer alignItems="center">
        {/* TODO: label을 어떻게 넣을것인가? 빈값으로? */}
        <Select name="period" value={time.period} onChange={onChange} required>
          <option value="AM">AM</option>
          <option value="PM">PM</option>
        </Select>
        <Select name="hour" value={time.hour} onChange={onChange} required>
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
        <Select name="minute" value={time.minute} onChange={onChange} required>
          <option value="00">00</option>
          <option value="30">30</option>
        </Select>
      </FlexContainer>
    </TextField>
  );
}

const StyledContent = styled.p`
  font-size: 3.2rem;
`;

export default AppointmentCreateFormTimeInput;
