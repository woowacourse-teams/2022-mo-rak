import styled from '@emotion/styled';
import React, { ChangeEventHandler } from 'react';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Input from '../../@common/Input/Input';

interface Props {
  closingTime: string;
  closingDate: string;
  onChangeTime: ChangeEventHandler<HTMLInputElement>;
  onChangeDate: ChangeEventHandler<HTMLInputElement>;
}

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
          required
        />
        <Input type="time" fontSize="1.6rem" value={closingTime} onChange={onChangeTime} required />
      </FlexContainer>
    </FlexContainer>
  );
}

const StyledLabel = styled.label(
  ({ theme }) => `
  font-size: 1.6rem;

  color: ${theme.colors.GRAY_400}
`
);
export default PollCreateCloseTimeInput;
