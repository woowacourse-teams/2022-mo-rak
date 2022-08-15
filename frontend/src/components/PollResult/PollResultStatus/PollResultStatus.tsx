import React from 'react';
import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import TextField from '../../@common/TextField/TextField';
import { PollInterface } from '../../../types/poll';

interface Props {
  status: PollInterface['status'];
}

function PollResultStatus({ status }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="filled"
      width="6.4rem"
      padding="1.6rem 0"
      borderRadius="1.2rem"
      colorScheme={status === 'OPEN' ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
    >
      <StyledStatus>{status === 'OPEN' ? '진행중' : '완료'}</StyledStatus>
    </TextField>
  );
}

const StyledStatus = styled.span(
  ({ theme }) => `
  color: ${theme.colors.WHITE_100};
  font-size:1.2rem;
`
);

export default PollResultStatus;
