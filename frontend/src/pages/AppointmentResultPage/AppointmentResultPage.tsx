import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { StyledContainer } from './AppointmentResultPage.style';

import { getAppointment, getAppointmentRecommendation } from '../../api/appointment';
import {
  AppointmentInterface,
  AppointmentRecommendationInterface,
  getAppointmentResponse
} from '../../types/appointment';
import { GroupInterface } from '../../types/group';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import AppointmentResultRanking from './components/AppointmentResultRanking/AppointmentResultRanking';
import AppointmentResultAvailableMembers from './components/AppointmentResultAvailableMembers/AppointmentResultAvailableMembers';
import AppointmentResultButtonGroup from './components/AppointmentResultButtonGroup/AppointmentResultButtonGroup';
import AppointmentResultHeader from './components/AppointmentResultHeader/AppointmentResultHeader';

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

  const handleShowParticipant = (idx: number) => () => {
    setClickedRecommendation(idx);
  };

  // TODO: 함수명 컨벤션에 대해서 생각해보자
  // 이벤트 핸들러 함수면 handle prefix가 맞는 것 같은데, 그게 아닌 함수에는 handle이 필요없는 것 같다.
  const setIsClosed = (isClosed: AppointmentInterface['isClosed']) => {
    // TODO: if appointment가 맞나?...없을 수도 있어서 undefined error가 발생
    if (appointment) {
      setAppointment({ ...appointment, isClosed });
    }
  };

  useEffect(() => {
    const fetchAppointmentRecommendation = async () => {
      try {
        const res = await getAppointmentRecommendation(groupCode, appointmentCode);
        setAppointmentRecommendation(res.data);
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
      <FlexContainer flexDirection="column" gap="4rem">
        <AppointmentResultHeader
          groupCode={groupCode}
          appointmentCode={appointmentCode}
          title={appointment.title}
          isClosed={appointment.isClosed}
          closedAt={appointment.closedAt}
        />
        <FlexContainer gap="4rem">
          <AppointmentResultRanking
            groupCode={groupCode}
            appointmentRecommendation={appointmentRecommendation}
            clickedRecommendation={clickedRecommendation}
            onClickRank={handleShowParticipant}
          />
          <AppointmentResultAvailableMembers
            appointmentRecommendation={appointmentRecommendation}
            clickedRecommendation={clickedRecommendation}
          />
        </FlexContainer>
        <AppointmentResultButtonGroup
          groupCode={groupCode}
          appointmentRecommendation={appointmentRecommendation}
          appointmentCode={appointmentCode}
          isClosed={appointment.isClosed}
          title={appointment.title}
          isHost={appointment.isHost}
          setIsClosed={setIsClosed}
        />
      </FlexContainer>
    </StyledContainer>
  );
}

export default AppointmentResultPage;
