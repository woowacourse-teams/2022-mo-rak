import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import { useParams } from 'react-router-dom';
import { getAppointment } from '../../../api/appointment';
import { AppointmentInterface, AppointmentInfoInterface } from '../../../types/appointment';
import { GroupInterface } from '../../../types/group';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import AppointmentResultRanking from '../AppointmentResultRanking';
import AppointmentResultButtonGroup from '../AppointmentResultButtonGroup';

function AppointmentResultContainer() {
  const [appointment, setAppointment] = useState<AppointmentInterface>();

  const { groupCode, appointmentCode } = useParams() as {
    groupCode: GroupInterface['code'];
    appointmentCode: AppointmentInfoInterface['code'];
  };

  useEffect(() => {
    const fetchAppointment = async () => {
      try {
        const res = await getAppointment(groupCode, appointmentCode);
        setAppointment(res);
      } catch (err) {
        alert(err);
      }
    };
    fetchAppointment();
  }, []);

  if (!appointment) return <div>로딩중입니다.</div>;

  return (
    <FlexContainer flexDirection="column" gap="4rem">
      <StyledTitle>{appointment.title}</StyledTitle>
      <StyledContent>모락은 가장 많이 겹치는 시간을 추천해드립니다🦔</StyledContent>
      {/* TODO: 항상 groupCode와 appointmentCode를 props로 내려주는 작업들이 반복되고 있다. 이를 전역으로 가지고 있어도 되지 않을까? */}
      <AppointmentResultRanking groupCode={groupCode} appointmentCode={appointmentCode} />
      <AppointmentResultButtonGroup groupCode={groupCode} appointmentCode={appointmentCode} />
    </FlexContainer>
  );
}

const StyledTitle = styled.h1`
  font-size: 4rem;
`;

const StyledContent = styled.p`
  font-size: 2.4rem;
  ef
`;

export default AppointmentResultContainer;
