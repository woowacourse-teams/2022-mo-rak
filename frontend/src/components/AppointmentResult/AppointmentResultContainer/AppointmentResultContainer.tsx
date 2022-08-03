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
  const [appointmentInfo, setAppointmentInfo] = useState<AppointmentInterface>({
    id: 1,
    title: '약속 잡기 제목',
    description: '약속 잡기 설명',
    startDate: '2022-07-26',
    endDate: '2022-08-04',
    startTime: '10:00AM',
    endTime: '10:00PM',
    durationHours: 2,
    durationMinutes: 30,
    isClosed: false
  });

  const { groupCode, appointmentCode } = useParams() as {
    groupCode: GroupInterface['code'];
    appointmentCode: AppointmentInfoInterface['code'];
  };

  useEffect(() => {
    const fetchAppointment = async () => {
      try {
        const res = await getAppointment(groupCode, appointmentCode);
        setAppointmentInfo(res);
      } catch (err) {
        alert(err);
      }
    };
    fetchAppointment();
  }, []);

  return (
    <div>
      {appointmentInfo ? (
        <FlexContainer flexDirection="column" gap="4rem">
          <StyledTitle>{appointmentInfo.title}</StyledTitle>
          <AppointmentResultRanking groupCode={groupCode} appointmentCode={appointmentCode} />
          <AppointmentResultButtonGroup />
        </FlexContainer>
      ) : (
        <div>로딩중</div>
      )}
    </div>
  );
}

const StyledTitle = styled.h1`
  font-size: 4rem;
`;

export default AppointmentResultContainer;