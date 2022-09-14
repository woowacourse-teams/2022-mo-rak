import styled from '@emotion/styled';
import { MouseEventHandler, useMemo } from 'react';
import Box from '../../@common/Box/Box';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { AppointmentInterface, AvailableTimes } from '../../../types/appointment';

const formatHourMinutePeriod = (date: Date) =>
  date
    .toLocaleString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true })
    .replace(' ', '');

const getTimeDetail = (time: string): [string, number, number] => {
  const period = time.slice(-2);
  const [hour, minute] = time.slice(0, -2).split(':');

  return [period, Number(hour), Number(minute)];
};

// NOTE: () => [{s: '10:00AM', e: '10:30AM' ....}]
const getTimeTables = (
  startTime: AppointmentInterface['startTime'],
  endTime: AppointmentInterface['endTime']
) => {
  const [startPeriod, startHour, startMinute] = getTimeDetail(startTime);
  const [endPeriod, endHour, endMinute] = getTimeDetail(endTime);

  const startDate = new Date(0, 0, 0, startHour, startMinute);
  if (startHour !== 12 && startPeriod === 'PM') {
    startDate.setHours(startHour + 12);
  }
  if (startHour === 12 && startPeriod === 'AM') {
    startDate.setHours(startHour - 12);
  }

  const endDate = new Date(0, 0, 0, endHour, endMinute);
  if ((endHour !== 12 && endPeriod === 'PM') || (endHour === 12 && endPeriod === 'AM')) {
    endDate.setHours(endHour + 12);
  }

  const timetables = [];
  while (startDate.getTime() !== endDate.getTime()) {
    const startTimeTable = startDate;
    const endTimeTable = new Date(startTimeTable.getTime() + 1800000);

    timetables.push({
      start: formatHourMinutePeriod(startTimeTable),
      end: formatHourMinutePeriod(endTimeTable)
    });

    startDate.setMinutes(startDate.getMinutes() + 30);
  }

  return timetables;
};

const getPlusOneDay = (date: string) => {
  const currentDate = new Date(date);
  currentDate.setDate(currentDate.getDate() + 1);

  return currentDate.toISOString().split('T')[0];
};

interface Props {
  startTime: AppointmentInterface['startTime'];
  endTime: AppointmentInterface['endTime'];
  selectedDate: string;
  // NOTE: onClickTime이라는 확장성을 열어둔 prop에 (start, end)를 특정해주게 되면 의미가 없지 않을까?
  onClickTime: (
    start: string,
    end: string
  ) => MouseEventHandler<HTMLDivElement> | MouseEventHandler<HTMLDivElement>;
  availableTimes: AvailableTimes;
}

// TODO: 위의 함수들이 여러개로 나뉘어져있는 것을 컴포넌트에 놔둬도 될지 고민해보기
function AppointmentProgressTimePicker({
  startTime,
  endTime,
  selectedDate,
  onClickTime,
  availableTimes
}: Props) {
  const timeTables = useMemo(() => getTimeTables(startTime, endTime), []);

  return (
    // TODO: box minheight 없애야할듯
    <Box width="30rem" minHeight="52rem" height="60rem" padding="3.6rem 2rem" overflow="auto">
      <FlexContainer flexDirection="column" gap="1.2rem">
        {!selectedDate ? (
          <StyledGuide>왼쪽에서 날짜를 선택해주세요~</StyledGuide>
        ) : (
          <>
            {timeTables.map(({ start, end }) => {
              // TODO: 리팩토링
              const endSelectedDate =
                end === '12:00AM' ? getPlusOneDay(selectedDate) : selectedDate;
              const isAvailableTime = availableTimes.some(
                (availableTime) =>
                  availableTime.start === `${selectedDate}T${start}` &&
                  availableTime.end === `${endSelectedDate}T${end}`
              );

              return (
                <StyledTime
                  key={`${start}-${end}`}
                  onClick={onClickTime(start, end)}
                  isSelected={isAvailableTime}
                >
                  {start}~{end}
                </StyledTime>
              );
            })}
          </>
        )}
      </FlexContainer>
    </Box>
  );
}

const StyledTime = styled.div<{ isSelected: boolean }>(
  ({ theme, isSelected }) => `
  width: 25.6rem;
  font-size: 2rem;
  text-align: center;
  padding: 1.2rem 0;
  border-radius: 10px;
  cursor: pointer;

  background-color: ${
    isSelected ? theme.colors.YELLOW_100 : theme.colors.TRANSPARENT_YELLOW_100_33
  };
  :hover {
    background-color: ${theme.colors.YELLOW_100};
  }
`
);

const StyledGuide = styled.p(
  ({ theme }) => `
  font-size: 2rem;
  text-align: center;
  line-height: 44.8rem;

  color: ${theme.colors.GRAY_400};
`
);
export default AppointmentProgressTimePicker;
