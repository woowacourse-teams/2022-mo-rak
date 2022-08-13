import styled from '@emotion/styled';
import React, { ChangeEventHandler } from 'react';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Input from '../../@common/Input/Input';

interface Props {
  closeTime: string;
  closeDate: string;
  onChangeTime: ChangeEventHandler<HTMLInputElement>;
  onChangeDate: ChangeEventHandler<HTMLInputElement>;
}

function PollCreateCloseTimeInput({ closeTime, closeDate, onChangeTime, onChangeDate }: Props) {
  return (
    <FlexContainer justifyContent="end" alignItems="center">
      <FlexContainer flexDirection="column" alignItems="end" gap="0.4rem">
        {/* TODO: 하나의 label 여러개 input */}
        <StyledLabel>마감시간</StyledLabel>
        <Input type="date" fontSize="1.6rem" value={closeDate} onChange={onChangeDate} required />
        <Input type="time" fontSize="1.6rem" value={closeTime} onChange={onChangeTime} required />
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
