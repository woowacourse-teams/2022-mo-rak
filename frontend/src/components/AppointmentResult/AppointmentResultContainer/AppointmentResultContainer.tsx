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
  // TODO: api 연결 전 화면 확인을 위해 초기값 임시 설정
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
      <StyledContent>가장 많은 시간이 겹치는 시간을 추천해줍니다</StyledContent>
      <AppointmentResultRanking groupCode={groupCode} appointmentCode={appointmentCode} />
      <AppointmentResultButtonGroup />
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
