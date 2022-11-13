import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  StyledContainer,
  StyledContentContainer
} from '@/pages/AppointmentResultPage/AppointmentResultPage.styles';
import { getAppointment, getAppointmentRecommendation } from '@/apis/appointment';
import {
  Appointment,
  AppointmentRecommendation,
  GetAppointmentResponse
} from '@/types/appointment';
import { Group } from '@/types/group';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import AppointmentResultRanking from '@/pages/AppointmentResultPage/components/AppointmentResultRanking/AppointmentResultRanking';
import AppointmentResultAvailableMembers from '@/pages/AppointmentResultPage/components/AppointmentResultAvailableMembers/AppointmentResultAvailableMembers';
import AppointmentResultButtons from '@/pages/AppointmentResultPage/components/AppointmentResultButtons/AppointmentResultButtons';
import AppointmentResultHeader from '@/pages/AppointmentResultPage/components/AppointmentResultHeader/AppointmentResultHeader';
import { AxiosError } from 'axios';
import Spinner from '@/components/Spinner/Spinner';

function AppointmentResultPage() {
  const navigate = useNavigate();
  const [appointment, setAppointment] = useState<GetAppointmentResponse>();
  const [appointmentRecommendation, setAppointmentRecommendation] = useState<
    Array<AppointmentRecommendation>
  >([]);
  const [clickedRecommendation, setClickedRecommendation] = useState<number>(-1);

  const { groupCode, appointmentCode } = useParams() as {
    groupCode: Group['code'];
    appointmentCode: Appointment['code'];
  };

  const handleShowParticipant = (idx: number) => () => {
    setClickedRecommendation(idx);
  };

  const setIsClosed = (isClosed: Appointment['isClosed']) => {
    // TODO: if appointment가 맞나?...없을 수도 있어서 undefined error가 발생
    if (appointment) {
      setAppointment({ ...appointment, isClosed });
    }
  };

  useEffect(() => {
    (async () => {
      const res = await getAppointmentRecommendation(groupCode, appointmentCode);
      setAppointmentRecommendation(res.data);
    })();
  }, []);

  useEffect(() => {
    (async () => {
      try {
        const res = await getAppointment(groupCode, appointmentCode);
        setAppointment(res.data);
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;

          if (errCode === '3300') {
            alert('존재하지 않는 약속잡기입니다.');
            navigate(`/groups/${groupCode}/appointment`);
          }
        }
      }
    })();
  }, []);

  return (
    <StyledContainer>
      {appointment ? (
        <StyledContentContainer>
          <AppointmentResultHeader
            groupCode={groupCode}
            appointmentCode={appointmentCode}
            title={appointment.title}
            isClosed={appointment.isClosed}
            closedAt={appointment.closedAt}
          />
          <FlexContainer gap="4rem" flexWrap="wrap">
            <AppointmentResultRanking
              appointmentRecommendation={appointmentRecommendation}
              clickedRecommendation={clickedRecommendation}
              onClickRank={handleShowParticipant}
            />
            <AppointmentResultAvailableMembers
              appointmentRecommendation={appointmentRecommendation}
              clickedRecommendation={clickedRecommendation}
            />
          </FlexContainer>
          <AppointmentResultButtons
            groupCode={groupCode}
            appointmentRecommendation={appointmentRecommendation}
            appointmentCode={appointmentCode}
            isClosed={appointment.isClosed}
            title={appointment.title}
            isHost={appointment.isHost}
            setIsClosed={setIsClosed}
          />
        </StyledContentContainer>
      ) : (
        <Spinner width="15%" placement="center" />
      )}
    </StyledContainer>
  );
}

export default AppointmentResultPage;
