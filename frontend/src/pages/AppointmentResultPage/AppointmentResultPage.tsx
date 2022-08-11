import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import { useParams } from 'react-router-dom';

import { getAppointment } from '../../api/appointment';
import { getAppointmentResponse, AppointmentInterface } from '../../types/appointment';
import { GroupInterface } from '../../types/group';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';
import AppointmentResultRanking from '../../components/AppointmentResult/AppointmentResultRanking/AppointmentResultRanking';
import AppointmentResultButtonGroup from '../../components/AppointmentResult/AppointmentResultButtonGroup/AppointmentResultButtonGroup';
import AppointmentResultHeader from '../../components/AppointmentResult/AppointmentResultHeader/AppointmentResultHeader';

function AppointmentResultPage() {
  const [appointment, setAppointment] = useState<getAppointmentResponse>();

  const { groupCode, appointmentCode } = useParams() as {
    groupCode: GroupInterface['code'];
    appointmentCode: AppointmentInterface['code'];
  };

  useEffect(() => {
    const fetchAppointment = async () => {
      try {
        const res = await getAppointment(groupCode, appointmentCode);
        setAppointment(res.data);
      } catch (err) {
        alert(err);
      }
    };

    fetchAppointment();
  }, []);

  if (!appointment) return <div>로딩중입니다.</div>;

  return (
    <StyledContainer>
      {/* TODO: 피그마를 기반으로 컴포넌트들 보여주기 */}
      <FlexContainer flexDirection="column" gap="4rem">
        <AppointmentResultHeader title={appointment.title} />
        <AppointmentResultRanking groupCode={groupCode} appointmentCode={appointmentCode} />
        <AppointmentResultButtonGroup groupCode={groupCode} appointmentCode={appointmentCode} />
      </FlexContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default AppointmentResultPage;
