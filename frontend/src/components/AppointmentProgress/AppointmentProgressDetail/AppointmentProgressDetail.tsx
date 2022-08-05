import styled from '@emotion/styled';
import React from 'react';
import { AppointmentInterface } from '../../../types/appointment';

interface Props {
  durationHours: AppointmentInterface['durationHours'];
  durationMinutes: AppointmentInterface['durationMinutes'];
  startTime: AppointmentInterface['startTime'];
  endTime: AppointmentInterface['endTime'];
}

function AppointmentProgressDetail({ durationHours, durationMinutes, startTime, endTime }: Props) {
  return (
    <>
      <StyledDuration>
        {durationHours}
        시간
        {durationMinutes}
        분동안 진행
      </StyledDuration>
      <StyledTimeRange>
        {startTime}~{endTime}
      </StyledTimeRange>
    </>
  );
}

const StyledDuration = styled.p`
  font-size: 4rem;
`;

const StyledTimeRange = styled.p`
  font-size: 3.2rem;
`;

export default AppointmentProgressDetail;
