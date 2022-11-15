import { useState } from 'react';
import AppointmentCreateForm from '@/pages/AppointmentCreatePage/components/AppointmentCreateForm/AppointmentCreateForm';
import Calendar from '@/components/Calendar/Calendar';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import {
  StyledLeftContainer,
  StyledRightContainer
} from '@/pages/AppointmentCreatePage/AppointmentCreatePage.styles';

function AppointmentCreateInputs() {
  const [startDate, setStartDate] = useState(''); // 2022-08-20 과 같은 형식이 들어옴 -> new Date()로 감싸서 사용 가능
  const [endDate, setEndDate] = useState('');

  return (
    <FlexContainer gap="4rem" flexWrap="wrap" justifyContent="center">
      <StyledLeftContainer>
        <Calendar
          startDate={startDate}
          endDate={endDate}
          setStartDate={setStartDate}
          setEndDate={setEndDate}
        />
      </StyledLeftContainer>
      <StyledRightContainer>
        <AppointmentCreateForm startDate={startDate} endDate={endDate} />
      </StyledRightContainer>
    </FlexContainer>
  );
}

export default AppointmentCreateInputs;
