import { useTheme } from '@emotion/react';

import styled from '@emotion/styled';

import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import TextField from '../../@common/TextField/TextField';
import { AppointmentInterface } from '../../../types/appointment';

const getFormattedClosedTime = (value: string) => {
  const date = new Date(value);

  return date.toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    hour12: true
  });
};

interface Props {
  durationHours: AppointmentInterface['durationHours'];
  durationMinutes: AppointmentInterface['durationMinutes'];
  closedAt: AppointmentInterface['closedAt'];
}

function AppointmentMainDetail({ durationHours, durationMinutes, closedAt }: Props) {
  const theme = useTheme();

  return (
    <FlexContainer flexDirection="column" gap="1.6rem">
      <StyledCloseTime>
        {getFormattedClosedTime(closedAt)}
        까지
      </StyledCloseTime>
      {/* TODO: flex 쓰지 않고 padding을 줄 수 있도록 만들어보기 */}
      <FlexContainer>
        <TextField
          padding="0.8rem 1.6rem" 
          borderRadius="20px"
          variant="outlined"
          colorScheme={theme.colors.PURPLE_100}
        >
          <StyledDetail>
            {durationHours}
            시간
            {durationMinutes.toString().padStart(2, '0')}분
          </StyledDetail>
        </TextField>
      </FlexContainer>
    </FlexContainer>
  );
}

const StyledCloseTime = styled.p`
  font-size: 1.4rem;
  align-self: end;
`;

const StyledDetail = styled.span(
  ({ theme }) => `
  color: ${theme.colors.PURPLE_100};
  font-size: 1.2rem;
`
);

export default AppointmentMainDetail;
