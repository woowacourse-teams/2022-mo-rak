import { Dispatch, SetStateAction, useState } from 'react';

import {
  StyledContainer,
  StyledCurrentMonthDay,
  StyledCurrentMonthDayNotInStartAndEndDate,
  StyledCurrentMonthDayPrevToday,
  StyledCurrentMonthDayToday,
  StyledDays,
  StyledMonth,
  StyledMonthTitle,
  StyledNextButton,
  StyledNextMonthDay,
  StyledPrevButton,
  StyledPrevMonthDay,
  StyledWeekDay,
  StyledWeekends
} from '@/components/Calendar/Calendar.styles';

type Props = {
  version?: 'default' | 'select';
  startDate: string;
  endDate: string;
  selectedDate?: string;
  setStartDate?: Dispatch<SetStateAction<string>>;
  setEndDate?: Dispatch<SetStateAction<string>>;
  setSelectedDate?: Dispatch<SetStateAction<string>>;
};

const weeks = ['일', '월', '화', '수', '목', '금', '토'];

// NOTE: 1차 리팩토링 끝
function Calendar({
  version = 'default',
  startDate,
  endDate,
  setStartDate,
  setEndDate,
  selectedDate,
  setSelectedDate
}: Props) {
  const [currentDate, setCurrentDate] = useState<Date>(new Date());
  const currentYear = currentDate.getFullYear();
  const currentMonth = currentDate.getMonth();
  const isCurrentMonth =
    new Date().getMonth() === currentMonth && new Date().getFullYear() === currentYear;

  const formatDate = (day: number) =>
    `${currentDate.getFullYear()}-${(currentDate.getMonth() + 1).toString().padStart(2, '0')}-${day
      .toString()
      .padStart(2, '0')}`;

  // NOTE: UI - 여기 있어도 될듯, date에 의존적이다 - start
  const getPrevMonthDays = () => {
    const copiedCurrentDate = new Date(currentDate.getTime());
    copiedCurrentDate.setDate(1);
    const firstDayIndex = copiedCurrentDate.getDay(); // 1일의 요일

    const prevMonthLastDay = new Date(currentYear, currentMonth, 0).getDate();

    return Array.from(
      { length: firstDayIndex },
      (v, i) => prevMonthLastDay - (firstDayIndex - i) + 1
    );
  };

  const getCurrentMonthDays = () => {
    const currentMonthLastDay = new Date(currentYear, currentMonth + 1, 0).getDate();

    return Array.from({ length: currentMonthLastDay }, (v, i) => i + 1);
  };

  const getNextMonthDays = () => {
    const lastDayIndex = new Date(currentYear, currentMonth + 1, 0).getDay(); // 막날의 요일
    const nextDays = 7 - lastDayIndex - 1; // 다음달 날짜

    return Array.from({ length: nextDays }, (v, i) => i + 1);
  };
  // NOTE: UI - 여기 있어도 될듯, currentDate에 의존적이다 - end

  const handleShowPrevMonth = () => {
    setCurrentDate(new Date(currentYear, currentMonth - 1, 1));
  };

  const handleShowNextMonth = () => {
    setCurrentDate(new Date(currentYear, currentMonth + 1, 1));
  };

  const handleStartOrEndDate = (day: number) => () => {
    if (!startDate) {
      setStartDate?.(formatDate(day));

      return;
    }

    if (endDate || isBeforeStartDate(day)) {
      setEndDate?.('');
      setStartDate?.(formatDate(day));

      return;
    }

    setEndDate?.(formatDate(day));
  };

  const handleSelectedDate = (day: number) => () => {
    setSelectedDate?.(formatDate(day));
  };

  const isPrevToday = (day: number) =>
    new Date(`${currentYear}-${currentMonth}-${day}`) <
    new Date(`${new Date().getFullYear()}-${new Date().getMonth()}-${new Date().getDate()}`);

  const isToday = (day: number) =>
    currentYear === new Date().getFullYear() &&
    currentMonth === new Date().getMonth() &&
    day === new Date().getDate();

  const isBetweenStartEndDate = (day: number) => {
    const targetDate = new Date(formatDate(day));

    return new Date(startDate) < targetDate && targetDate < new Date(endDate);
  };

  const isStartOrEndDate = (day: number) =>
    startDate === formatDate(day) || endDate === formatDate(day);

  const isBeforeStartDate = (day: number) =>
    new Date(`${currentYear}-${currentMonth + 1}-${day}`) < new Date(startDate);

  const isNotInStartAndEndDate = (day: number) => {
    const targetDate = new Date(formatDate(day));

    return new Date(startDate) > targetDate || targetDate > new Date(endDate);
  };

  const isSelectedDate = (day: number) => formatDate(day) === selectedDate;

  return (
    <StyledContainer>
      <StyledMonth>
        <StyledPrevButton
          onClick={handleShowPrevMonth}
          disabled={isCurrentMonth}
          aria-label="prev-month"
        >
          &#8249;
        </StyledPrevButton>
        <StyledMonthTitle>
          {currentDate.getFullYear()}년 {currentMonth + 1}월
        </StyledMonthTitle>
        <StyledNextButton onClick={handleShowNextMonth} aria-label="next-month">
          &#8250;
        </StyledNextButton>
      </StyledMonth>
      <StyledWeekends>
        {weeks.map((day) => (
          <StyledWeekDay key={day}>{day}</StyledWeekDay>
        ))}
      </StyledWeekends>
      <StyledDays>
        {getPrevMonthDays().map((day) => (
          <StyledPrevMonthDay key={day} onClick={handleShowPrevMonth} isHidden={isCurrentMonth}>
            {day}
          </StyledPrevMonthDay>
        ))}

        {version === 'select'
          ? getCurrentMonthDays().map((day) => {
              if (isNotInStartAndEndDate(day)) {
                return (
                  <StyledCurrentMonthDayNotInStartAndEndDate
                    key={day}
                    aria-label={`${String(currentMonth + 1).padStart(2, '0')}-${String(
                      day
                    ).padStart(2, '0')}`}
                  >
                    {day}
                  </StyledCurrentMonthDayNotInStartAndEndDate>
                );
              }

              return (
                <StyledCurrentMonthDay
                  key={day}
                  onClick={handleSelectedDate(day)}
                  isSelectedDate={isSelectedDate(day)}
                  aria-label={`${String(currentMonth + 1).padStart(2, '0')}-${String(day).padStart(
                    2,
                    '0'
                  )}`}
                >
                  {day}
                </StyledCurrentMonthDay>
              );
            })
          : getCurrentMonthDays().map((day) => {
              if (isPrevToday(day)) {
                return (
                  <StyledCurrentMonthDayPrevToday
                    key={day}
                    aria-label={`${String(currentMonth + 1).padStart(2, '0')}-${String(
                      day
                    ).padStart(2, '0')}`}
                  >
                    {day}
                  </StyledCurrentMonthDayPrevToday>
                );
              }

              if (isToday(day)) {
                return (
                  <StyledCurrentMonthDayToday
                    key={day}
                    onClick={setStartDate && setEndDate && handleStartOrEndDate(day)}
                    isBetweenStartEndDate={isBetweenStartEndDate(day)}
                    isStartOrEndDate={isStartOrEndDate(day)}
                    aria-label={`${String(currentMonth + 1).padStart(2, '0')}-${String(
                      day
                    ).padStart(2, '0')}`}
                  >
                    {day}
                  </StyledCurrentMonthDayToday>
                );
              }

              return (
                <StyledCurrentMonthDay
                  key={day}
                  onClick={handleStartOrEndDate(day)}
                  isBetweenStartEndDate={isBetweenStartEndDate(day)}
                  isStartOrEndDate={isStartOrEndDate(day)}
                  aria-label={`${String(currentMonth + 1).padStart(2, '0')}-${String(day).padStart(
                    2,
                    '0'
                  )}`}
                >
                  {day}
                </StyledCurrentMonthDay>
              );
            })}

        {getNextMonthDays().map((day) => (
          <StyledNextMonthDay key={day} onClick={handleShowNextMonth}>
            {day}
          </StyledNextMonthDay>
        ))}
      </StyledDays>
    </StyledContainer>
  );
}

export default Calendar;
