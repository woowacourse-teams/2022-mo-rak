import { SetStateAction, useState, Dispatch } from 'react';
import styled from '@emotion/styled';

interface Props {
  version?: 'default' | 'select';
  startDate: string;
  endDate: string;
  selectedDate?: string;
  setStartDate?: Dispatch<SetStateAction<string>>;
  setEndDate?: Dispatch<SetStateAction<string>>;
  setSelectedDate?: Dispatch<SetStateAction<string>>;
}

const weeks = ['일', '월', '화', '수', '목', '금', '토'];

// version "default"는 약속 잡기 생성에서 사용된다. 기본 version이 default로 설정되어있기 때문에,
// 별도로 props로 version을 넘겨주지 않아도 된다.
// version "select"는 약속 잡기 선택하기에서 사용된다. 이때, props로 startDate, endDate가 넘어와야한다.
// NOTE: version에 기반하여 컴포넌트가 둘로 나뉘어지기 때문에, 이는 두 가지 역할을 하나의 컴포넌트가 하는 게 아닐까 싶다.
// 만약 version이 하나 더 생긴다면 로직이 복잡해질 것 같다. version을 없애는 게 첫 번째 리팩토링 포인트일듯
// NOTE: 1차 리팩토링 끝
// TODO: calendar 리팩토링
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
  const isCurrentMonth = new Date().getMonth() === currentMonth;

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
    // TODO: 약속잡기 진행페이지에서는 사용되지 않기 때문에, 분기처리를 해줌
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

  const isNotBetweenStartAndEndDate = (day: number) => {
    const targetDate = new Date(formatDate(day));

    return new Date(startDate) > targetDate || targetDate > new Date(endDate);
  };

  const isSelectedDate = (day: number) => formatDate(day) === selectedDate;

  return (
    <StyledCalendar>
      <StyledMonth>
        <StyledPrevButton onClick={handleShowPrevMonth} disabled={isCurrentMonth}>
          {/* TODO: 이런 방식이 괜찮은 방법인지 고민하기 */}
          &#8249;
        </StyledPrevButton>
        <StyledMonthTitle>
          {currentDate.getFullYear()}년 {currentMonth + 1}월
        </StyledMonthTitle>
        <StyledNextButton onClick={handleShowNextMonth}>&#8250;</StyledNextButton>
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
              if (isNotBetweenStartAndEndDate(day)) {
                return (
                  // TODO: 변수명 생각해보기
                  <StyledCurrentMonthDayNotInStartAndEndDate key={day}>
                    {day}
                  </StyledCurrentMonthDayNotInStartAndEndDate>
                );
              }

              return (
                <StyledCurrentMonthDay
                  key={day}
                  onClick={handleSelectedDate(day)}
                  isSelectedDate={isSelectedDate(day)}
                >
                  {day}
                </StyledCurrentMonthDay>
              );
            })
          : getCurrentMonthDays().map((day) => {
              if (isPrevToday(day)) {
                return (
                  <StyledCurrentMonthDayPrevToday key={day}>{day}</StyledCurrentMonthDayPrevToday>
                );
              }

              if (isToday(day)) {
                return (
                  <StyledCurrentMonthDayToday
                    // TODO: 약속잡기 진행페이지에서는 사용되지 않기 때문에, 분기처리를 해줌
                    key={day}
                    onClick={setStartDate && setEndDate && handleStartOrEndDate(day)}
                    isBetweenStartEndDate={isBetweenStartEndDate(day)}
                    isStartOrEndDate={isStartOrEndDate(day)}
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
    </StyledCalendar>
  );
}

const StyledCalendar = styled.div(
  ({ theme }) => `
  width: 45.2rem;
  height: 60rem;
  border-radius: 2rem;

  background: ${theme.colors.WHITE_100};
`
);

const StyledMonth = styled.div`
  height: 12rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
  text-align: center;
`;

const StyledWeekends = styled.div`
  height: 5.2rem;
  padding: 0 0.4rem;
  display: flex;
  align-items: center;
`;

const StyledWeekDay = styled.div`
  font-size: 1.6rem;
  width: calc(45.2rem / 7);
  display: flex;
  justify-content: center;
`;

const StyledPrevButton = styled.button`
  font-size: 4.4rem;
  opacity: 0.5;
`;

const StyledNextButton = styled.button`
  font-size: 4.4rem;
  opacity: 0.5;
`;

const StyledMonthTitle = styled.h1`
  font-size: 2.4rem;
`;

const StyledDays = styled.div`
  display: flex;
  flex-wrap: wrap;
  padding: 0.4rem;
  cursor: pointer;
`;

const StyledMonthDay = styled.div`
  font-size: 1.6rem;
  margin: 0.3rem;
  width: calc(40.2rem / 7);
  height: calc(40.2rem / 7);
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.2s;
`;

// (4단위로 맞지 않는 부분이 있는데, 4단위로 맞추면 깨지는 이슈...)
// TODO: 현재 달력에서는 이전달, 다음달 날짜 일부를 보여줄 필요가 없어서 화면에서 보이지 않도록 처리.
// (이후 필요할 시, props로 option을 받아서 보이도록 해줄 수 있음)
const StyledPrevMonthDay = styled(StyledMonthDay)<{ isHidden: boolean }>(
  ({ theme, isHidden }) => `
  color: ${theme.colors.GRAY_300};
  ${isHidden && 'visibility: hidden'};
`
);

const StyledNextMonthDay = styled(StyledMonthDay)(
  ({ theme }) => `
  color: ${theme.colors.GRAY_300};
`
);

// NOTE: version이 default, select에 따라 분기가 일어나므로 복잡
const StyledCurrentMonthDay = styled(StyledMonthDay)<{
  isBetweenStartEndDate?: boolean;
  isStartOrEndDate?: boolean;
  isSelectedDate?: boolean;
}>(
  ({ theme, isBetweenStartEndDate, isStartOrEndDate, isSelectedDate }) => `
  color: ${theme.colors.BLACK_100};

  &:hover {
    border: 2px solid ${theme.colors.PURPLE_100};
    border-radius: 100%;
  }
  
  ${
    isSelectedDate
      ? `background-color: ${theme.colors.PURPLE_100};
    color: ${theme.colors.WHITE_100};
    border-radius: 100%;`
      : ''
  }

  ${
    isStartOrEndDate
      ? `background-color: ${theme.colors.PURPLE_100};
  color: ${theme.colors.WHITE_100};
  border-radius: 100%;`
      : ''
  }

  ${
    isBetweenStartEndDate
      ? `background-color: ${theme.colors.PURPLE_50};
    border-radius: 100%;
    `
      : ''
  }
`
);

const StyledCurrentMonthDayPrevToday = styled(StyledCurrentMonthDay)(
  ({ theme }) => `
  color: ${theme.colors.GRAY_200};
`
);

const StyledCurrentMonthDayToday = styled(StyledCurrentMonthDay)``;

const StyledCurrentMonthDayNotInStartAndEndDate = styled(StyledCurrentMonthDay)(
  ({ theme }) => `
  &:hover {
    border: none;
  }

  color: ${theme.colors.GRAY_200};
`
);

export default Calendar;
