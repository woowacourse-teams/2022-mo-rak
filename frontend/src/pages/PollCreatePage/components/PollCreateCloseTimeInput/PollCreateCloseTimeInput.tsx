import { StyledLabel } from '@/pages/PollCreatePage/components/PollCreateCloseTimeInput/PollCreateCloseTimeInput.styles';
import { ChangeEventHandler, memo } from 'react';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import Input from '@/components/Input/Input';

type Props = {
  closingTime: string;
  closingDate: string;
  onChangeTime: ChangeEventHandler<HTMLInputElement>;
  onChangeDate: ChangeEventHandler<HTMLInputElement>;
};

function PollCreateCloseTimeInput({ closingTime, closingDate, onChangeTime, onChangeDate }: Props) {
  const [today] = new Date().toISOString().split('T');

  return (
    <FlexContainer justifyContent="end" alignItems="center">
      <FlexContainer flexDirection="column" alignItems="end" gap="0.4rem">
        {/* TODO: 하나의 label 여러개 input은 html에서 권장하지 않는 것이니 해결 필요 */}
        <StyledLabel>마감시간</StyledLabel>
        <Input
          type="date"
          fontSize="1.6rem"
          value={closingDate}
          min={today}
          onChange={onChangeDate}
          aria-label="poll-closingDate"
          role="textbox"
          required
        />
        <Input
          type="time"
          fontSize="1.6rem"
          value={closingTime}
          onChange={onChangeTime}
          aria-label="poll-closingTime"
          role="textbox"
          required
        />
      </FlexContainer>
    </FlexContainer>
  );
}

export default memo(
  PollCreateCloseTimeInput,
  (prev, next) => prev.closingDate === next.closingDate && prev.closingTime === next.closingTime
);
