import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState, useMemo } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getAppointment, progressAppointment } from '../../api/appointment';
import Box from '../../components/common/Box/Box';
import Button from '../../components/common/Button/Button';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';
import { GroupInterface } from '../../types/group';
import { AppointmentInterface } from '../../types/appointment';
import Calendar from '../../components/common/Calendar/Calendar';

// TODO: 로직 리팩토링...엉망임
// NOTE: () => [{s: '10:00AM', e: '10:30AM' ....}]
const getTimes = (
  startTime: AppointmentInterface['startTime'],
  endTime: AppointmentInterface['endTime']
) => {
  // NOTE: 여기서 AM, PM 고려해서 시, 분을 포맷해준다.
  const formatHM = (sh: number, sm: number) => {
    const period = Math.floor(sh / 12) >= 1 ? 'PM' : 'AM';
    let formatedSh = period === 'PM' ? sh % 12 : sh;
    formatedSh = formatedSh === 0 ? 12 : formatedSh;

    return `${String(formatedSh).padStart(2, '0')}:${String(sm).padStart(2, '0')}${period}`;
  };

  const sTime = startTime.slice(0, -2);
  const sPeriod = startTime.slice(-2);
  const [sh, sm] = sTime.split(':');

  const eTime = endTime.slice(0, -2);
  const ePeriod = endTime.slice(-2);
  const [eh, em] = eTime.split(':');

  const startHM = new Date();
  if (sPeriod === 'PM') {
    startHM.setHours(Number(sh) + 12);
  } else {
    startHM.setHours(Number(sh));
  }
  startHM.setMinutes(Number(sm));

  const endHM = new Date();
  if ((Number(eh) !== 12 && ePeriod === 'PM') || (Number(eh) === 12 && ePeriod === 'AM')) {
    endHM.setHours(Number(eh) + 12);
  } else {
    endHM.setHours(Number(eh));
  }
  endHM.setMinutes(Number(em));

  const timetables = [];

  while (startHM.getTime() !== endHM.getTime()) {
    const s = [startHM.getHours(), startHM.getMinutes()];
    startHM.setMinutes(startHM.getMinutes() + 30);
    const e = [startHM.getHours(), startHM.getMinutes()];

    timetables.push({
      s: formatHM(s[0], s[1]),
      e: formatHM(e[0], e[1])
    });
  }

  return timetables;
};

// TODO: 리팩토링 (데모데이때문에 급하게 함)
// TODO: 페이지 추상화
function AppointmentProgressPage() {
  const theme = useTheme();
  const navigate = useNavigate();
  const { groupCode, appointmentCode } = useParams() as {
    groupCode: GroupInterface['code'];
    appointmentCode: string;
  };
  const [appointment, setAppointment] = useState<AppointmentInterface>();
  const [selectedDate, setSelectedDate] = useState(''); // version="select"에서, 사용자가 선택한 날짜
  const [availableTimes, setAvailableTimes] = useState<Array<{ start: string; end: string }>>([]);
  const times = useMemo(() => {
    if (appointment) {
      return getTimes(appointment.startTime, appointment.endTime);
    }

    return [];
  }, [selectedDate]);

  // TODO: 로직 효율화
  const handleAvailableTimes = (s: string, e: string) => () => {
    if (!selectedDate) {
      alert('날짜를 선택해주세요!');

      return;
    }

    const isSelected = availableTimes.find(
      ({ start, end }) => start === `${selectedDate}T${s}` && end === `${selectedDate}T${e}`
    );

    if (isSelected) {
      const filteredAvailableTimes = availableTimes.filter((time) => time !== isSelected);

      setAvailableTimes(filteredAvailableTimes);

      return;
    }

    setAvailableTimes([
      ...availableTimes,
      {
        start: `${selectedDate}T${s}`,
        end: `${selectedDate}T${e}`
      }
    ]);
  };

  const handleSubmit = async () => {
    try {
      await progressAppointment(groupCode, appointmentCode, availableTimes);
      navigate(`/groups/${groupCode}/appointment/${appointmentCode}/result`);
    } catch (err) {
      console.log(err);
    }
  };

  // NOTE: 데이터 불러오는 것을 즉시실행함수로 실행하면 코드 가독량을 줄일 수 있을듯
  useEffect(() => {
    (async () => {
      try {
        const res = await getAppointment(groupCode, appointmentCode);
        setAppointment(res);
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
        {/* header-start */}
        <StyledHeaderContainer>
          <StyledHeader>{appointment.title}</StyledHeader>
          <StyledDescription>{appointment.description}</StyledDescription>
        </StyledHeaderContainer>
        {/* header-end */}
        <Calendar
          version="select"
          startDate={appointment.startDate}
          endDate={appointment.endDate}
          selectedDate={selectedDate}
          setSelectedDate={setSelectedDate}
        />
      </StyledLeftContainer>
      <StyledRightContainer>
        {/* detail-start */}
        <StyledDuration>
          {appointment.durationHours}
          시간
          {appointment.durationMinutes}
          분동안 진행
        </StyledDuration>
        <StyledTimeRange>
          {appointment.startTime}~{appointment.endTime}
        </StyledTimeRange>
        {/* detail-end */}
        {/* timePicker-start */}
        <Box width="30rem" height="58rem" padding="3.6rem 2rem" overflow="auto">
          <FlexContainer flexDirection="column" gap="1.2rem">
            {times.map(({ s, e }) => (
              <StyledTime
                onClick={handleAvailableTimes(s, e)}
                isSelected={availableTimes.some(
                  ({ start, end }) =>
                    start === `${selectedDate}T${s}` && end === `${selectedDate}T${e}`
                )}
              >
                {s}~{e}
              </StyledTime>
            ))}
          </FlexContainer>
        </Box>
        {/* timePicker-end */}
        {/* submitButton-start */}
        <Button
          variant="filled"
          colorScheme={theme.colors.PURPLE_100}
          width="31.6rem"
          fontSize="4rem"
          onClick={handleSubmit}
        >
          선택
        </Button>
        {/* submitButton-end */}
      </StyledRightContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  width: calc(100% - 36.4rem);
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

const StyledHeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 3.6rem;
  max-height: 20.8rem;
  min-height: 20.8rem;
`;

const StyledHeader = styled.header`
  font-size: 4.8rem;
`;

const StyledDescription = styled.div`
  font-size: 2rem;
  overflow-y: scroll;
`;

const StyledRightContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 38.8rem;
  gap: 1.2rem;
  align-items: center;
`;

const StyledDuration = styled.p`
  font-size: 4rem;
`;

const StyledTimeRange = styled.p`
  font-size: 3.2rem;
`;

const StyledTime = styled.div<{ isSelected: boolean }>(
  ({ theme, isSelected }) => `
  width: 25.6rem;
  font-size: 1.6rem;
  background-color: ${
    isSelected ? theme.colors.YELLOW_100 : theme.colors.TRANSPARENT_YELLOW_100_33
  };
  text-align: center;
  padding: 0.8rem 0;
  border-radius: 5px;
  cursor: pointer;

  

  :hover {
    background-color: ${theme.colors.YELLOW_100};
  }
`
);

export default AppointmentProgressPage;
