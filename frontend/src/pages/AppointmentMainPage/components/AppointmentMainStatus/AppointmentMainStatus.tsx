import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import TextField from '../../../../components/TextField/TextField';

interface Props {
  isClosed: boolean;
}

function AppointmentMainStatus({ isClosed }: Props) {
  const theme = useTheme();

  return (
    <TextField
      variant="filled"
      width="7.2rem"
      padding="0.8rem 0"
      borderRadius="5px"
      colorScheme={!isClosed ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
    >
      <StyledStatus>{!isClosed ? '진행중' : '완료'}</StyledStatus>
    </TextField>
  );
}

const StyledStatus = styled.span(
  ({ theme }) => `
  color: ${theme.colors.WHITE_100};
  font-size: 1.2rem;
`
);

export default AppointmentMainStatus;
