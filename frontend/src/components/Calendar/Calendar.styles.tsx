import styled from '@emotion/styled';

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
  border-radius: 100%;

  &:hover {
    border: 2px solid ${theme.colors.PURPLE_100};
  }
  
  ${
    isSelectedDate
      ? `background-color: ${theme.colors.PURPLE_100};
    color: ${theme.colors.WHITE_100};`
      : ''
  }

  ${
    isStartOrEndDate
      ? `background-color: ${theme.colors.PURPLE_100};
  color: ${theme.colors.WHITE_100};`
      : ''
  }

  ${isBetweenStartEndDate ? `background-color: ${theme.colors.PURPLE_50};` : ''}
`
);

const StyledCurrentMonthDayPrevToday = styled(StyledCurrentMonthDay)(
  ({ theme }) => `
  &:hover {
    border: none;
  }

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

export {
  StyledCalendar,
  StyledMonth,
  StyledWeekends,
  StyledWeekDay,
  StyledPrevButton,
  StyledNextButton,
  StyledMonthTitle,
  StyledDays,
  StyledMonthDay,
  StyledPrevMonthDay,
  StyledNextMonthDay,
  StyledCurrentMonthDay,
  StyledCurrentMonthDayPrevToday,
  StyledCurrentMonthDayToday,
  StyledCurrentMonthDayNotInStartAndEndDate
};
