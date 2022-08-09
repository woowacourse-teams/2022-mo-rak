import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import { useParams } from 'react-router-dom';

import { getAppointment, getAppointmentRecommendation } from '../../api/appointment';
import { getAppointmentResponse, AppointmentInterface, AppointmentRecommendationInterface } from '../../types/appointment';
import { GroupInterface } from '../../types/group';
import FlexContainer from '../../components/@common/FlexContainer/FlexContainer';
import AppointmentResultRanking from '../../components/AppointmentResult/AppointmentResultRanking/AppointmentResultRanking';
import AppointmentResultAvailableMembers from '../../components/AppointmentResult/AppointmentResultAvailableMembers/AppointmentResultAvailableMembers';
import AppointmentResultButtonGroup from '../../components/AppointmentResult/AppointmentResultButtonGroup/AppointmentResultButtonGroup';
import AppointmentResultHeader from '../../components/AppointmentResult/AppointmentResultHeader/AppointmentResultHeader';

function AppointmentResultPage() {
  const [appointment, setAppointment] = useState<getAppointmentResponse>();
  const [appointmentRecommendation, setAppointmentRecommendation] = useState<
  Array<AppointmentRecommendationInterface>
  >([]);
  const [clickedRecommendation, setClickedRecommendation] = useState<number>(-1);

  const { groupCode, appointmentCode } = useParams() as {
    groupCode: GroupInterface['code'];
    appointmentCode: AppointmentInterface['code'];
  };

  useEffect(() => {
    const fetchAppointmentRecommendation = async () => {
      try {
        const res = await getAppointmentRecommendation(groupCode, appointmentCode);
        setAppointmentRecommendation(res);
      } catch (err) {
        alert(err);
      }
    };
    fetchAppointmentRecommendation();
  }, []);

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

  const handleShowParticipant = (idx: number) => () => {
    setClickedRecommendation(idx);
  };

  if (!appointment) return <div>로딩중입니다.</div>;

  return (
    <StyledContainer>
      {/* TODO: 피그마를 기반으로 컴포넌트들 보여주기 */}
      <FlexContainer flexDirection="column" gap="4rem">
        <AppointmentResultHeader title={appointment.title} />
        <FlexContainer gap="4rem">
          <AppointmentResultRanking
            groupCode={groupCode}
            appointmentRecommendation={appointmentRecommendation}
            onClickRank={handleShowParticipant}
            clickedRecommendation={clickedRecommendation}
          />
          <AppointmentResultAvailableMembers
            appointmentRecommendation={appointmentRecommendation}
            clickedRecommendation={clickedRecommendation}
          />
        </FlexContainer>
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
