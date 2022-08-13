import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useParams } from 'react-router-dom';

import Box from '../../@common/Box/Box';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import MarginContainer from '../../@common/MarginContainer/MarginContainer';
import { GroupInterface } from '../../../types/group';
import { getAppointmentsResponse } from '../../../types/appointment';
import { getAppointments } from '../../../api/appointment';
import AppointmentMainStatus from '../AppointmentMainStatus/AppointmentMainStatus';
import AppointmentMainProgress from '../AppointmentMainProgress/AppointmentMainProgress';
import AppointmentMainDetail from '../AppointmentMainDetail/AppointmentMainDetail';
import AppointmentMainButtonGroup from '../AppointmentMainButtonGroup/AppointmentMainButtonGroup';

function AppointmentMainContainer() {
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const [appointments, setAppointments] = useState<getAppointmentsResponse>([]);

  useEffect(() => {
    const fetchAppointments = async () => {
      const res = await getAppointments(groupCode);
      setAppointments(res.data);
    };

    try {
      fetchAppointments();
    } catch (err) {
      alert(err);
    }
  }, []);

  return (
    <StyledContainer>
      {appointments ? (
        appointments.map(({
          code,
          title,
          description,
          durationHours,
          durationMinutes,
          count,
          isClosed }) => (
            <Box
              width="26.4rem"
              padding="2rem"
              minHeight="16.8rem"
              filter={isClosed ? 'grayscale(1)' : 'none'}
            >
              <FlexContainer justifyContent="end">
                <AppointmentMainStatus isClosed={isClosed} />
              </FlexContainer>
              <StyledTitle>{title}</StyledTitle>
              <StyledDescription>{description}</StyledDescription>
              <AppointmentMainProgress count={count} groupCode={groupCode} />
              <MarginContainer margin="0 0 1.2rem">
                <AppointmentMainDetail
                  durationHours={durationHours}
                  durationMinutes={durationMinutes}
                />
              </MarginContainer>
              <AppointmentMainButtonGroup appointmentCode={code} isClosed={isClosed} />
            </Box>
        ))
      ) : (
        <div>없습니다</div>
      )}
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  column-gap: 3.2rem;
  row-gap: 3.2rem;
`;

const StyledTitle = styled.h1`
  font-size: 1.6rem;
  text-align: center;
`;

const StyledDescription = styled.div`
  font-size: 0.8rem;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

export default AppointmentMainContainer;
