import styled from '@emotion/styled';
import React, { MouseEventHandler, useMemo } from 'react';
import Box from '../../../components/common/Box/Box';
import FlexContainer from '../../../components/common/FlexContainer/FlexContainer';
import { AppointmentInterface } from '../../../types/appointment';

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
  if (
    (Number(endHour) !== 12 && startPeriod === 'PM') ||
    (Number(endHour) === 12 && startPeriod === 'AM')
  ) {
    startHM.setHours(Number(startHour) + 12);
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
    const s = [startHM.getHours(), startHM.getMinutes()];
    startHM.setMinutes(startHM.getMinutes() + 30);
    const e = [startHM.getHours(), startHM.getMinutes()];

    timetables.push({
      start: formatHM(s[0], s[1]),
      end: formatHM(e[0], e[1])
    });
  }

  return timetables;
};

interface Props {
  startTime: AppointmentInterface['startTime'];
  endTime: AppointmentInterface['endTime'];
  selectedDate: string;
  onClickTime: (start: string, end: string) => MouseEventHandler<HTMLDivElement>;
  availableTimes: Array<{ start: string; end: string }>;
}

function AppointmentProgressTimePicker({
  startTime,
  endTime,
  selectedDate,
  onClickTime,
  availableTimes
}: Props) {
  const times = useMemo(() => {
    if (startTime && endTime && selectedDate) {
      return getTimes(startTime, endTime);
    }

    return [];
  }, [selectedDate]);

  return (
    <Box width="30rem" height="58rem" padding="3.6rem 2rem" overflow="auto">
      <FlexContainer flexDirection="column" gap="1.2rem">
        {!times.length && <StyledGuideText>왼쪽에서 날짜를 선택해주세요~</StyledGuideText>}
        {times.map(({ start, end }) => {
          return (
            <StyledTime
              onClick={onClickTime(start, end)}
              isSelected={availableTimes.some(
                (availableTime) =>
                  availableTime.start === `${selectedDate}T${start}` &&
                  availableTime.end === `${selectedDate}T${end}`
              )}
            >
              {start}~{end}
            </StyledTime>
          );
        })}
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
  border-radius: 5px;
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
  line-height: 58rem;

  color: ${theme.colors.GRAY_400}
`
);
export default AppointmentProgressTimePicker;
