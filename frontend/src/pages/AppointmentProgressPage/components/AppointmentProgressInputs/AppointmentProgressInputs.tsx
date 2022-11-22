import { useState } from 'react';
import { progressAppointment } from '@/apis/appointment';
import { useNavigate } from 'react-router-dom';
import { AvailableTimes, Appointment } from '@/types/appointment';
import Calendar from '@/components/Calendar/Calendar';
import AppointmentProgressButtons from '@/pages/AppointmentProgressPage/components/AppointmentProgressButtons/AppointmentProgressButtons';
import {
  StyledLeftContainer,
  StyledRightContainer
} from '@/pages/AppointmentProgressPage/components/AppointmentProgressInputs/AppointmentProgressInputs.styles';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import AppointmentProgressTimePicker from '../AppointmentProgressTimePicker/AppointmentProgressTimePicker';
import { AxiosError } from 'axios';
import { Group } from '@/types/group';

type Props = {
  appointment: Appointment;
  groupCode: Group['code'];
  appointmentCode: Appointment['code'];
};

// TODO: 중복됨 제거
const getPlusOneDate = (date: string) => {
  const currentDate = new Date(date);
  currentDate.setDate(currentDate.getDate() + 1);

  return currentDate.toISOString().split('T')[0];
};

// TODO: getPlusOneDate랑 합쳐도 좋을듯?
const getMinusOneDate = (date: string) => {
  const currentDate = new Date(date);
  currentDate.setDate(currentDate.getDate() - 1);

  return currentDate.toISOString().split('T')[0];
};

function AppointmentProgressInputs({ appointment, groupCode, appointmentCode }: Props) {
  const navigate = useNavigate();
  const [selectedDate, setSelectedDate] = useState('');
  const [availableTimes, setAvailableTimes] = useState<AvailableTimes>([]);

  // TODO: 로직 효율화
  const handleAvailableTimes = (start: string, end: string) => () => {
    const endSelectedDate = end === '12:00AM' ? getPlusOneDate(selectedDate) : selectedDate;

    const isSelected = availableTimes.find(
      (availableTime) =>
        availableTime.start === `${selectedDate}T${start}` &&
        availableTime.end === `${endSelectedDate}T${end}`
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
        end: `${endSelectedDate}T${end}`
      }
    ]);
  };

  const handleProgressAppointment = async () => {
    try {
      await progressAppointment(groupCode, appointmentCode, availableTimes);
      navigate(`/groups/${groupCode}/appointment/${appointmentCode}/result`);
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        switch (errCode) {
          case '4000': {
            alert('현재보다 과거의 시간을 선택할 수 없습니다');

            break;
          }

          case '3100': {
            alert('마감된 약속잡기이므로 약속잡기를 진행할 수 없습니다');
            navigate(`/groups/${groupCode}/appointment`);

            break;
          }

          case '3300': {
            alert('존재하지 않는 약속잡기이므로 약속잡기를 진행할 수 없습니다');
            navigate(`/groups/${groupCode}/appointment`);

            break;
          }
        }
      }
    }
  };

  return (
    <FlexContainer gap="4rem" flexWrap="wrap" justifyContent="center">
      <StyledLeftContainer>
        <Calendar
          version="select"
          startDate={appointment.startDate}
          endDate={
            appointment.endTime === '12:00AM'
              ? getMinusOneDate(appointment.endDate)
              : appointment.endDate
          }
          selectedDate={selectedDate}
          // TODO: setSelectedDate를 넘겨주는 것이 아니라 onClickDay 같이 해주는 게 어떨까? props를 받는 Calendar 컴포넌트에서는
          // 위에서 어떤 함수가 내려오는 지 정확한 이름을 알 필요가 없다. 바깥에는 onClickDay 같이 소통할 수 있는 인터페이스만 제공해주면 될뿐
          setSelectedDate={setSelectedDate}
        />
      </StyledLeftContainer>
      <StyledRightContainer>
        <AppointmentProgressTimePicker
          startTime={appointment.startTime}
          endTime={appointment.endTime}
          selectedDate={selectedDate}
          onClickTime={handleAvailableTimes}
          availableTimes={availableTimes}
        />
        <AppointmentProgressButtons onClickProgress={handleProgressAppointment} />
      </StyledRightContainer>
    </FlexContainer>
  );
}

export default AppointmentProgressInputs;
