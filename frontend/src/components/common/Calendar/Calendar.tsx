import React, { useState } from 'react';
import styled from '@emotion/styled';

// 이전, 다음 버튼 구현
// 버전 -> today를 보여주는가?(지금 잠시 주석처리) , 이전 날짜를 이동할 수 없게 할 것인가? (글자disabled, 버튼 disabled)
function Calendar() {
  const [date, setDate] = useState<Date>(new Date()); 
  const [startDate, setStartDate] = useState(''); // new Date로 감싸서 사용하면 됨 
  const [endDate, setEndDate] = useState('');
  const weeks = ['일', '월', '화', '수', '목', '금', '토'];

  const getPrevMonthDays = () => {
    const days = [];
    const prevLastDay = new Date(date.getFullYear(), date.getMonth(), 0).getDate(); // 6월 말일
    date.setDate(1);
    const firstDayIndex = date.getDay(); // 1일의 요일

    for (let x = firstDayIndex; x > 0; x--) {
      days.push(prevLastDay - x + 1);
    }

    return days;
  };

  const getNowMonthDays = () => {
    const days = [];
    const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate(); // 7월 말일

    for (let i = 1; i <= lastDay; i++) {
        days.push(i);
    }

    return days;
  };

  const getNextMonthDays = () => {
    const days = [];
    const lastDayIndex = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDay(); // 막날의 요일
    const nextDays = 7 - lastDayIndex - 1; // 다음달 날짜

    for (let j = 1; j <= nextDays; j++) {
      days.push(j);
    }

    return days;
  };

  const handleShowPrevMonth = () => {
    const nowDate = date;
    setDate(new Date(nowDate.getFullYear(), nowDate.getMonth() - 1, 1))
  };

  const handleShowNextMonth = () => {
    const nowDate = date;
    setDate(new Date(nowDate.getFullYear(), nowDate.getMonth() + 1, 1))
  };

  const handleClickDay = (day: number) => () =>  {
    // 마지막 날이 선택 되어 있는 경우 -> 새로운 start 값을 설정해줘야함 
    if(endDate !== '') {
      setEndDate('');
      setStartDate(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`);
      return;
    }
    // 첫번째 날이 선택 되어 있는 경우 -> end값을 설정해줘야함 
    if(startDate !== '' && endDate === '') {
      setEndDate(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`);
      return;
    }
    setStartDate(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`); // 가장 처음 값을 설정해줌 
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
        {getPrevMonthDays().map((day) => (<StyledPrevMonthDays>{day}</StyledPrevMonthDays>))}
        {getNowMonthDays().map((day) => {
          if (new Date(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`) < new Date(`${new Date().getFullYear()}-${new Date().getMonth() + 1}-${new Date().getDate()}`)) {
            return (<StyledPrevToday>{day}</StyledPrevToday>)
          }
          if (date.getFullYear() === new Date().getFullYear() && date.getMonth() === new Date().getMonth() && day === new Date().getDate()) {
            return (<StyledTodayMonthDays>{day}</StyledTodayMonthDays>)
          }
          return (
          <StyledNowMonthDays 
            onClick={handleClickDay(day)} 
            isBetweenSelectedDate={new Date(startDate) < new Date(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`) && new Date(`${date.getFullYear()}-${date.getMonth() + 1}-${day}`) < new Date(endDate)} 
            isSelectedDate={startDate === `${date.getFullYear()}-${date.getMonth() + 1}-${day}` || endDate === `${date.getFullYear()}-${date.getMonth() + 1}-${day}`} 
          >
              {day}
          </StyledNowMonthDays>)
        })}
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

// 중복 (rightButton)
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

const StyledDays = styled.div(({ theme }) => `
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

  // 클릭되면 filled로 채워주기 
  div:hover {
    border: 2px solid ${theme.colors.PURPLE_100};
    border-radius: 100%;
  }
`);

const StyledPrevMonthDays = styled.div(({ theme }) => `
  color: ${theme.colors.GRAY_200};
`);

const StyledNextMonthDays = styled.div(({ theme }) => `
  color: ${theme.colors.GRAY_200};
`);

const StyledNowMonthDays = styled.div<{
  isBetweenSelectedDate: boolean;
  isSelectedDate: boolean;
}>(({ theme, isBetweenSelectedDate, isSelectedDate }) => `
  color: ${theme.colors.BLACK_100};
  
  // 선택 된 startDate, endDate라면 
  ${ isSelectedDate? 
    `background-color: ${theme.colors.PURPLE_100};
    color: ${theme.colors.WHITE_100};
    border-radius: 100%;` : ''
  }

  // 선택 된 startDate, endDate 사이에 있으면 
  // color theme으로 빼기 
  ${ isBetweenSelectedDate?
    `background-color: #EEEDFF;
    border-radius: 100%;
    ` : ''
  }
`);

const StyledTodayMonthDays = styled.div(({ theme }) => `
  // background-color: ${theme.colors.PURPLE_100};
  color: ${theme.colors.PURPLE_100};
  border-radius: 100%;
`);

const StyledPrevToday = styled.div(({theme}) => `
  color: ${theme.colors.GRAY_200};
`);

export default Calendar;
