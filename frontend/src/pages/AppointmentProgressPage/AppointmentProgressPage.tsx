import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getAppointment } from '@/apis/appointment';
import { Group } from '@/types/group';
import { Appointment, GetAppointmentResponse } from '@/types/appointment';
import AppointmentProgressHeader from '@/pages/AppointmentProgressPage/components/AppointmentProgressHeader/AppointmentProgressHeader';
import AppointmentProgressInputs from '@/pages/AppointmentProgressPage/components/AppointmentProgressInputs/AppointmentProgressInputs';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import { StyledContainer } from '@/pages/AppointmentProgressPage/AppointmentProgressPage.styles';
import { AxiosError } from 'axios';
import Spinner from '@/components/Spinner/Spinner';

// TODO: 사용자 경험 향상을 위해, 과거의 시간은 선택할 수 없도록 disabled 필요
function AppointmentProgressPage() {
  const navigate = useNavigate();
  const { groupCode, appointmentCode } = useParams() as {
    groupCode: Group['code'];
    appointmentCode: Appointment['code'];
  };
  const [appointment, setAppointment] = useState<GetAppointmentResponse>();

  useEffect(() => {
    (async () => {
      try {
        const res = await getAppointment(groupCode, appointmentCode);
        if (res.data.isClosed) {
          alert('마감된 약속잡기입니다');
          navigate(`/groups/${groupCode}/appointment`);

          return;
        }

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
        <FlexContainer flexDirection="column" gap="2rem">
          <AppointmentProgressHeader appointment={appointment} />
          <AppointmentProgressInputs
            appointment={appointment}
            groupCode={groupCode}
            appointmentCode={appointmentCode}
          />
           
        </FlexContainer>
      ) : (
        <Spinner width="15%" placement="center" />
      )}
    </StyledContainer>
  );
}

export default AppointmentProgressPage;
