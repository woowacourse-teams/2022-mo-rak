import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getAppointment, progressAppointment } from '../../api/appointment';
import { GroupInterface } from '../../types/group';
import { AvailableTimes, AppointmentInterface } from '../../types/appointment';
import Calendar from '../../components/@common/Calendar/Calendar';
import AppointmentProgressHeader from '../../components/AppointmentProgress/AppointmentProgressHeader/AppointmentProgressHeader';
import AppointmentProgressDetail from '../../components/AppointmentProgress/AppointmentProgressDetail/AppointmentProgressDetail';
import AppointmentProgressTimePicker from '../../components/AppointmentProgress/AppointmentProgressTimePicker/AppointmentProgressTimePicker';
import AppointmentProgressButtonGroup from '../../components/AppointmentProgress/AppointmentProgressButtonGroup/AppointmentProgressButtonGroup';

// TODO: 리팩토링 (데모데이때문에 급하게 함)
// TODO: 페이지 추상화
function AppointmentProgressPage() {
  const navigate = useNavigate();
  const { groupCode, appointmentCode } = useParams() as {
    groupCode: GroupInterface['code'];
    appointmentCode: AppointmentInterface['code'];
  };

  const [appointment, setAppointment] = useState<AppointmentInterface>();
  // TODO: 가장 빠른 날짜가 기본값으로 설정되도록 해주자
  const [selectedDate, setSelectedDate] = useState(''); // version="select"에서, 사용자가 선택한 날짜
  const [availableTimes, setAvailableTimes] = useState<AvailableTimes>([]);

  // TODO: 로직 효율화
  const handleAvailableTimes = (start: string, end: string) => () => {
    if (!selectedDate) {
      alert('날짜를 선택해주세요!');

      return;
    }

    const isSelected = availableTimes.find(
      (availableTime) =>
        availableTime.start === `${selectedDate}T${start}` &&
        availableTime.end === `${selectedDate}T${end}`
    );

    if (isSelected) {
      const filteredAvailableTimes = availableTimes.filter(
        (availableTime) => availableTime !== isSelected
      );

      setAvailableTimes(filteredAvailableTimes);

      return;
    }

    setAvailableTimes([
      ...availableTimes,
      {
        start: `${selectedDate}T${start}`,
        end: `${selectedDate}T${end}`
      }
    ]);
  };

  const handleProgressAppointment = async () => {
    try {
      await progressAppointment(groupCode, appointmentCode, availableTimes);

      navigate(`/groups/${groupCode}/appointment/${appointmentCode}/result`);
    } catch (err) {
      console.log(err);
    }
  };

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
      } catch (error) {
        console.log(error);
      }
    })();
  }, []);

  if (!appointment) return <div>로딩중입니다</div>;

  // NOTE: 이렇게 Left,Right가 있을 때는 어떻게 추상화 레벨을 맞춰줄까?...
  return (
    <StyledContainer>
      <StyledLeftContainer>
        <AppointmentProgressHeader
          title={appointment.title}
          description={appointment.description}
        />
        <Calendar
          version="select"
          startDate={appointment.startDate}
          endDate={appointment.endDate}
          selectedDate={selectedDate}
          // TODO: setSelectedDate를 넘겨주는 것이 아니라 onClickDay 같이 해주는 게 어떨까? props를 받는 Calendar 컴포넌트에서는
          // 위에서 어떤 함수가 내려오는 지 정확한 이름을 알 필요가 없다. 바깥에는 onClickDay 같이 소통할 수 있는 인터페이스만 제공해주면 될뿐
          setSelectedDate={setSelectedDate}
        />
      </StyledLeftContainer>
      <StyledRightContainer>
        <AppointmentProgressDetail
          durationHours={appointment.durationHours}
          durationMinutes={appointment.durationMinutes}
          startTime={appointment.startTime}
          endTime={appointment.endTime}
        />
        <AppointmentProgressTimePicker
          startTime={appointment.startTime}
          endTime={appointment.endTime}
          selectedDate={selectedDate}
          onClickTime={handleAvailableTimes}
          availableTimes={availableTimes}
        />
        <AppointmentProgressButtonGroup onClickProgress={handleProgressAppointment} />
      </StyledRightContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 18.4rem;
`;

const StyledLeftContainer = styled.div`
  display: flex;
  width: 45.2rem;
  flex-direction: column;
  gap: 1.8rem;
`;

const StyledRightContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 38.8rem;
  gap: 1.2rem;
  align-items: center;
`;

export default AppointmentProgressPage;
