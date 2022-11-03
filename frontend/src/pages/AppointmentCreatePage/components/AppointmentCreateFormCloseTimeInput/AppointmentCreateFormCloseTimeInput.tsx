import { ChangeEventHandler, memo } from 'react';
import { useTheme } from '@emotion/react';
import Input from '@/components/Input/Input';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import TextField from '@/components/TextField/TextField';
import { StyledTitle } from './AppointmentCreateFormCloseTimeInput.styles';

type Props = {
  // TODO: 투표와 변수명 맞춰기
  closeTime: string;
  closeDate: string;
  maxCloseDate: string;
  onChangeTime: ChangeEventHandler<HTMLInputElement>;
  onChangeDate: ChangeEventHandler<HTMLInputElement>;
};

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
            role="textbox"
            aria-label="appointment-closeDate"
            required
          />
          <Input
            type="time"
            onChange={onChangeTime}
            value={closeTime}
            fontSize="2.4rem"
            role="textbox"
            aria-label="appointment-closeTime"
            required
          />
        </FlexContainer>
      </TextField>
    </>
  );
}

export default memo(
  AppointmentCreateFormCloseTimeInput,
  (prev, next) =>
    prev.closeTime === next.closeTime &&
    prev.closeDate === next.closeDate &&
    prev.maxCloseDate === next.maxCloseDate
);
