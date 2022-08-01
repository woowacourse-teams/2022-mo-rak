import React, { useState } from 'react';
import styled from '@emotion/styled';

function Calendar() {
  const [date, setDate] = useState<Date>(new Date()); 
  const [startDate, setStartDate] = useState(''); // 2022-08-20 과 같은 형식이 들어옴 -> new Date()로 감싸서 사용 가능
  const [endDate, setEndDate] = useState('');
  const weeks = ['일', '월', '화', '수', '목', '금', '토'];

  const getPrevMonthDays = () => {
    const prevLastDay = new Date(date.getFullYear(), date.getMonth(), 0).getDate(); // 지난달 말일
    date.setDate(1);
    const firstDayIndex = date.getDay(); // 1일의 요일

    return Array.from({length: firstDayIndex}, (v, i) => prevLastDay - (firstDayIndex - i) + 1);
  };

  const getNowMonthDays = () => {
    const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate(); // 이번달 말일

    return Array.from({length: lastDay}, (v, i) => i + 1);

  };

  const getNextMonthDays = () => {
    const lastDayIndex = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDay(); // 막날의 요일
    const nextDays = 7 - lastDayIndex - 1; // 다음달 날짜

    return Array.from({length: nextDays}, (v, i) => i + 1);
  };

  const handleShowPrevMonth = () => {
    const nowDate = date;
    setDate(new Date(nowDate.getFullYear(), nowDate.getMonth() - 1, 1))
  };

  const handleShowNextMonth = () => {
    const nowDate = date;
    setDate(new Date(nowDate.getFullYear(), nowDate.getMonth() + 1, 1))
  };

  const handleSetStartOrEndDate = (day: number) => () =>  {
    // 마지막 날이 선택 되어 있는 경우 -> 새로운 start 값을 설정해줘야함 
    if (endDate !== '') {
      setEndDate('');
      setStartDate(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`);
      return;
    }
    // 첫번째 날이 선택 되어 있는 경우 -> end값을 설정해줘야함 
    if (startDate !== '' && endDate === '') {
      setEndDate(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`);
      return;
    }
    setStartDate(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`); // 가장 처음 값을 설정해줌 
  };

  const isPrevToday = (day) => {
    return new Date(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`) < new Date(`${new Date().getFullYear()}-${new Date().getMonth() + 1}-${new Date().getDate()}`);
  };
  
  const isToday = (day) => {
    return date.getFullYear() === new Date().getFullYear() && date.getMonth() === new Date().getMonth() && day === new Date().getDate();
  };

  const isBetweenStartEndDate = (day) => {
    return new Date(startDate) < new Date(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`) && new Date(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`) < new Date(endDate);
  }

  const isStartOrEndDate = (day) => {
    return startDate === `${date.getFullYear()}-${date.getMonth() + 1}-${day}` || endDate === `${date.getFullYear()}-${date.getMonth() + 1}-${day}`;
  }

  return (
    <StyledCalendar>
      <StyledMonth>
        <StyledPrevButton onClick={handleShowPrevMonth} disabled={new Date().getMonth() === date.getMonth()}>&#8249;</StyledPrevButton> 
        <StyledMonthTitle>{`${date.getFullYear()}년 ${date.getMonth() + 1}월`}</StyledMonthTitle>
        <StyledNextButton onClick={handleShowNextMonth}>&#8250;</StyledNextButton>
      </StyledMonth>
      <StyledWeekends>
        {weeks.map((day) => <StyledWeekDay>{day}</StyledWeekDay>)}
      </StyledWeekends>
      <StyledDays>
        {/* 지난 달 일부 날짜 */}
        {getPrevMonthDays().map((day) => (<StyledPrevMonthDays>{day}</StyledPrevMonthDays>))}

        {/* 이번 달 날짜  */}
        {getNowMonthDays().map((day) => {
          if (isPrevToday(day)) {
            return (
            <StyledNowMonthDays type="prev">{day}</StyledNowMonthDays>)
          }
          if (isToday(day)) {
            return (
            <StyledNowMonthDays 
              type="today"
              onClick={handleSetStartOrEndDate(day)} 
              isBetweenStartEndDate={isBetweenStartEndDate(day)} 
              isStartOrEndDate={isStartOrEndDate(day)} 
            >
              {day}
            </StyledNowMonthDays>)
          }
          return (
          <StyledNowMonthDays 
            onClick={handleSetStartOrEndDate(day)} 
            isBetweenStartEndDate={isBetweenStartEndDate(day)} 
            isStartOrEndDate={isStartOrEndDate(day)} 
          >
            {day}
          </StyledNowMonthDays>)
        })}

        {/* 다음 달 일부 날짜  */}
        {getNextMonthDays().map((day) => (<StyledNextMonthDays>{day}</StyledNextMonthDays>))}
      </StyledDays>
    </StyledCalendar>
  );
}

// css 점검 (4단위, height 없애기)
const StyledCalendar = styled.div(({ theme }) => `
  width: 45.2rem;
  height: 60rem;
  background: ${theme.colors.WHITE_100};
  border-radius: 2rem;
`);

const StyledMonth = styled.div`
  width: 100%;
  height: 12rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
  text-align: center;
`;

const StyledWeekends = styled.div`
  width: 100%;
  height: 5.2rem;
  padding: 0 0.4rem;
  display: flex;
  align-items: center;
`;

const StyledWeekDay = styled.div`
  font-size: 1.6rem;
  font-weight: 400;
  letter-spacing: 0.12rem;
  width: calc(44.2rem / 7);
  display: flex;
  justify-content: center;
  align-items: center;
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
  font-weight: 400;
  text-transform: uppercase;
`;

const StyledDays = styled.div`
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  padding: 0.4rem;
  cursor: pointer;

  div {
    font-size: 1.6rem;
    // 0.4하면 깨짐 
    margin: 0.3rem; 
    width: calc(40.2rem / 7);
    height: calc(40.2rem / 7);
    display: flex;
    justify-content: center;
    align-items: center;
    transition: background-color 0.2s;
  }
`;

const StyledPrevMonthDays = styled.div(({ theme }) => `
  color: ${theme.colors.GRAY_200}; 
`);

const StyledNextMonthDays = styled.div(({ theme }) => `
  color: ${theme.colors.GRAY_200};
`);

const StyledNowMonthDays = styled.div<{
  isBetweenStartEndDate?: boolean;
  isStartOrEndDate?: boolean;
  type?: string;
}>(({ theme, isBetweenStartEndDate, isStartOrEndDate, type }) => `
  color: ${theme.colors.BLACK_100};
  
  // 선택 된 startDate, endDate라면 
  ${ isStartOrEndDate? 
    `background-color: ${theme.colors.PURPLE_100};
    color: ${theme.colors.WHITE_100};
    border-radius: 100%;` : ''
  }

  // 선택 된 startDate, endDate 사이에 있으면 
  // color theme으로 빼기 
  ${ isBetweenStartEndDate?
    `background-color: #EEEDFF;
    border-radius: 100%;
    ` : ''
  }

  ${type === 'prev'?
    `color: ${theme.colors.GRAY_200};` : ''
  }

  ${type === 'today'?
  `color: ${theme.colors.YELLOW_100};` : ''
  }

  &:hover {
    border: 2px solid ${theme.colors.PURPLE_100};
    border-radius: 100%;
  }
`);


export default Calendar;
