import { ChangeEventHandler, memo } from 'react';
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
      <StyledTitle>마감 시간 설정</StyledTitle>
      <TextField
        variant="outlined"
        colorScheme={theme.colors.PURPLE_100}
        width="42rem"
        padding="0.4rem 0.8rem"
        borderRadius="1.2rem"
      >
        <FlexContainer alignItems="center">
          <Input
            type="date"
            min={today}
            max={maxCloseDate}
            onChange={onChangeDate}
            value={closeDate}
            fontSize="2.4rem"
            required
          />
          <Input type="time" onChange={onChangeTime} value={closeTime} fontSize="2.4rem" required />
        </FlexContainer>
      </TextField>
    </>
  );
}

const StyledTitle = styled.div(
  ({ theme }) => `
  font-size: 2.8rem;
  color: ${theme.colors.PURPLE_100};
`
);

export default memo(
  AppointmentCreateFormCloseTimeInput,
  (prev, next) =>
    prev.closeTime === next.closeTime &&
    prev.closeDate === next.closeTime &&
    prev.maxCloseDate === next.maxCloseDate
);
