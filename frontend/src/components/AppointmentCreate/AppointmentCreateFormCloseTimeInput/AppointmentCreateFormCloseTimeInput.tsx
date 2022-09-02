import { ChangeEventHandler } from 'react';
import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';
import Input from '../../@common/Input/Input';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import TextField from '../../@common/TextField/TextField';

interface Props {
  closeTime: string;
  closeDate: string;
  maxCloseDate: string;
  onChangeTime: ChangeEventHandler<HTMLInputElement>;
  onChangeDate: ChangeEventHandler<HTMLInputElement>;
}

function AppointmentCreateFormCloseTimeInput({
  closeTime,
  closeDate,
  maxCloseDate,
  onChangeTime,
  onChangeDate
}: Props) {
  const theme = useTheme();
  const [today] = new Date().toISOString().split('T');

  return (
    <>
      <StyledLabel>마감 시간 설정</StyledLabel>
      <TextField
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
        width="36rem"
        padding="0.8rem"
        borderRadius="10px"
      >
        <FlexContainer alignItems="center">
          <Input
            type="date"
            min={today}
            max={maxCloseDate}
            onChange={onChangeDate}
            value={closeDate}
            fontSize="2rem"
            required
          />
          <Input type="time" onChange={onChangeTime} value={closeTime} fontSize="2rem" required />
        </FlexContainer>
      </TextField>
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
