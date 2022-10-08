import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useLottie } from 'lottie-react';
import Box from '../../../../components/Box/Box';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import MarginContainer from '../../../../components/MarginContainer/MarginContainer';
import { GroupInterface } from '../../../../types/group';
import { getAppointmentsResponse } from '../../../../types/appointment';
import { getAppointments } from '../../../../api/appointment';
import AppointmentMainStatus from '../AppointmentMainStatus/AppointmentMainStatus';
import AppointmentMainProgress from '../AppointmentMainProgress/AppointmentMainProgress';
import AppointmentMainDetail from '../AppointmentMainDetail/AppointmentMainDetail';
import AppointmentMainButtonGroup from '../AppointmentMainButtonGroup/AppointmentMainButtonGroup';
import emptyAnimation from '../../../../assets/empty-animation.json';
import {
  StyledContainer,
  StyledTitle,
  LottieWrapper,
  StyledGuide
} from './AppointmentMainContainer.styles';

function AppointmentMainContainer() {
  const emptyLottie = useLottie({ animationData: emptyAnimation }, { width: '60rem' });
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
        <LottieWrapper>{emptyLottie.View}</LottieWrapper>
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
              key={code}
              width="36.4rem"
              padding="2.8rem"
              minHeight="23.2rem"
              filter={isClosed ? 'grayscale(1)' : 'none'}
            >
              <FlexContainer justifyContent="end">
                <AppointmentMainStatus isClosed={isClosed} />
              </FlexContainer>
              <StyledTitle>{title}</StyledTitle>
              <MarginContainer margin="0 0 0.4rem">
                <AppointmentMainProgress count={count} groupCode={groupCode} />
              </MarginContainer>
              <MarginContainer margin="0 0 1.6rem">
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

export default AppointmentMainContainer;
