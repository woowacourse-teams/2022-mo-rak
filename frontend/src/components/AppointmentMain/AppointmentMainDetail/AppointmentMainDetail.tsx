import { useTheme } from '@emotion/react';
import React from 'react';
import styled from '@emotion/styled';

import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import TextField from '../../@common/TextField/TextField';
import { AppointmentInterface } from '../../../types/appointment';

interface Props {
  durationHours: AppointmentInterface['durationHours'];
  durationMinutes: AppointmentInterface['durationMinutes'];
}

function AppointmentMainDetail({ durationHours, durationMinutes }: Props) {
  const theme = useTheme();

  return (
    <FlexContainer gap="1.2rem">
      <TextField
        width="3.6rem"
        borderRadius="20px"
        variant="outlined"
        padding="0.4rem 0"
        colorScheme={theme.colors.PURPLE_100}
      >
        <StyledDetail>
          {durationHours}
          시간
          {durationMinutes}
          분
        </StyledDetail>
      </TextField>
    </FlexContainer>
  );
}

const StyledDetail = styled.span(
  ({ theme }) => `
  color: ${theme.colors.PURPLE_100};
  font-size: 0.8rem;
`
);

export default AppointmentMainDetail;
