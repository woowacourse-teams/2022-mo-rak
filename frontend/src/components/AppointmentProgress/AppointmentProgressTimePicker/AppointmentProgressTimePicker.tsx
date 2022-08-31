import styled from '@emotion/styled';
import React, { MouseEventHandler, useMemo } from 'react';
import Box from '../../@common/Box/Box';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { AppointmentInterface, AvailableTimes } from '../../../types/appointment';

// TODO: 로직 리팩토링...엉망임
// NOTE: () => [{s: '10:00AM', e: '10:30AM' ....}]
const getTimes = (
  startTime: AppointmentInterface['startTime'],
  endTime: AppointmentInterface['endTime']
) => {
  // NOTE: 여기서 AM, PM 고려해서 시, 분을 포맷해준다.
  const formatHM = (hour: number, minute: number) => {
    const period = Math.floor(hour / 12) >= 1 ? 'PM' : 'AM';
    let formatedHour = period === 'PM' ? hour % 12 : hour;
    formatedHour = formatedHour === 0 ? 12 : formatedHour;

    return `${String(formatedHour).padStart(2, '0')}:${String(minute).padStart(2, '0')}${period}`;
  };

  const startPeriod = startTime.slice(-2);
  const [startHour, startMinute] = startTime.slice(0, -2).split(':');

  const endPeriod = endTime.slice(-2);
  const [endHour, endMinute] = endTime.slice(0, -2).split(':');

  const startHM = new Date();
  if (Number(startHour) !== 12 && startPeriod === 'PM') {
    startHM.setHours(Number(startHour) + 12);
  } else if (Number(startHour) === 12 && startPeriod === 'AM') {
    startHM.setHours(Number(startHour) - 12);
  } else {
    startHM.setHours(Number(startHour));
  }

  startHM.setMinutes(Number(startMinute));

  const endHM = new Date();
  if (
    (Number(endHour) !== 12 && endPeriod === 'PM') ||
    (Number(endHour) === 12 && endPeriod === 'AM')
  ) {
    endHM.setHours(Number(endHour) + 12);
  } else {
    endHM.setHours(Number(endHour));
  }

  endHM.setMinutes(Number(endMinute));

  const timetables = [];

  while (startHM.getTime() !== endHM.getTime()) {
    const [SH, SM] = [startHM.getHours(), startHM.getMinutes()];
    startHM.setMinutes(startHM.getMinutes() + 30);
    const [EH, EM] = [startHM.getHours(), startHM.getMinutes()];

    timetables.push({
      start: formatHM(SH, SM),
      end: formatHM(EH, EM)
    });
  }

  return timetables;
};

// TODO: 중복됨 제거
const getPlusOneDate = (date: string) => {
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

function AppointmentProgressTimePicker({
  startTime,
  endTime,
  selectedDate,
  onClickTime,
  availableTimes
}: Props) {
  const times = useMemo(() => getTimes(startTime, endTime), []);

  return (
    // TODO: box minheight 없애야할듯
    <Box width="30rem" minHeight="52rem" height="52rem" padding="3.6rem 2rem" overflow="auto">
      <FlexContainer flexDirection="column" gap="1.2rem">
        {!selectedDate ? (
          <StyledGuideText>왼쪽에서 날짜를 선택해주세요~</StyledGuideText>
        ) : (
          <>
            {times.map(({ start, end }) => {
              // TODO: 리팩토링
              const endSelectedDate =
                end === '12:00AM' ? getPlusOneDate(selectedDate) : selectedDate;

              return (
                <StyledTime
                  key={`${start}-${end}`}
                  onClick={onClickTime(start, end)}
                  isSelected={availableTimes.some(
                    (availableTime) =>
                      availableTime.start === `${selectedDate}T${start}` &&
                      availableTime.end === `${endSelectedDate}T${end}`
                  )}
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
  font-size: 1.6rem;
  text-align: center;
  padding: 0.8rem 0;
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

const StyledGuideText = styled.p(
  ({ theme }) => `
  font-size: 2rem;
  text-align: center;
  line-height: 44.8rem;

  color: ${theme.colors.GRAY_400};
`
);
export default AppointmentProgressTimePicker;
