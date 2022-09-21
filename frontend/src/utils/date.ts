const getFormattedDateTime = (dateTimeInput: string) => {
  // TODO: 리팩토링
  const period = dateTimeInput.slice(-2);
  const dateTime = new Date(dateTimeInput.slice(0, -2));
  const week = ['일', '월', '화', '수', '목', '금', '토'];

  const year = dateTime.getFullYear();
  const month = dateTime.getMonth() + 1;
  const date = dateTime.getDate();
  const day = week[dateTime.getDay()];
  const hour = dateTime.getHours().toString().padStart(2, '0');
  const minutes = dateTime.getMinutes().toString().padStart(2, '0');

  return `${year}.${month}.${date}(${day}) ${hour}:${minutes}${period}`;
};

export { getFormattedDateTime };
