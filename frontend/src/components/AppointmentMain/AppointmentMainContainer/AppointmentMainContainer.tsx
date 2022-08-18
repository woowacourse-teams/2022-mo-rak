import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useParams } from 'react-router-dom';
import { useLottie } from 'lottie-react';
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
import emptyAnimation from '../../../assets/empty-animation.json';

function AppointmentMainContainer() {
  const { View: EmptyView } = useLottie({ animationData: emptyAnimation }, { width: '60rem' });
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

  if (appointments.length <= 0) {
    return (
      <>
        {/* TODO: 재사용 가능하지 않을까한다! */}
        <LottieWrapper>{EmptyView}</LottieWrapper>
        <StyledGuide>첫 약속잡기를 만들어보세요!</StyledGuide>
      </>
    );
  }

  return (
    <StyledContainer>
      {appointments.length > 0 &&
        appointments.map(
          ({ code, title, durationHours, durationMinutes, count, isClosed, closedAt }) => (
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
              <MarginContainer margin="0 0 0.4rem">
                <AppointmentMainProgress count={count} groupCode={groupCode} />
              </MarginContainer>
              <MarginContainer margin="0 0 1.2rem">
                <AppointmentMainDetail
                  durationHours={durationHours}
                  durationMinutes={durationMinutes}
                  closedAt={closedAt}
                />
              </MarginContainer>
              <AppointmentMainButtonGroup appointmentCode={code} isClosed={isClosed} />
            </Box>
          )
        )}
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2.4rem;
`;

const StyledTitle = styled.h1`
  font-size: 1.6rem;
  text-align: center;
`;

const LottieWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const StyledGuide = styled.p(
  ({ theme }) => `
  text-align: center;
  font-size: 2.8rem;

  color: ${theme.colors.GRAY_400}
`
);
export default AppointmentMainContainer;
