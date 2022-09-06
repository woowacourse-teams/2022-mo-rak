import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import TextField from '../../@common/TextField/TextField';
import { AppointmentInterface } from '../../../types/appointment';

interface Props {
  isClosed: AppointmentInterface['isClosed'];
}

function AppointmentResultStatus({ isClosed }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="filled"
      width="6.8rem"
      padding="1.6rem 0"
      borderRadius="1.2rem"
      colorScheme={isClosed ? theme.colors.GRAY_400 : theme.colors.PURPLE_100}
    >
      <StyledStatus>{isClosed ? '완료' : '진행중'}</StyledStatus>
    </TextField>
  );
}

const StyledStatus = styled.span(
  ({ theme }) => `
  color: ${theme.colors.WHITE_100};
  font-size: 1.6rem;
`
);

export default AppointmentResultStatus;
