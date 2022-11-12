import { useState } from 'react';

import {
  StyledContainer,
  StyledContentContainer,
  StyledLeftContainer,
  StyledRightContainer
} from '@/pages/AppointmentCreatePage/AppointmentCreatePage.styles';
import AppointmentCreateForm from '@/pages/AppointmentCreatePage/components/AppointmentCreateForm/AppointmentCreateForm';
import AppointmentCreateHeader from '@/pages/AppointmentCreatePage/components/AppointmentCreateHeader/AppointmentCreateHeader';

import Calendar from '@/components/Calendar/Calendar';
import FlexContainer from '@/components/FlexContainer/FlexContainer';

function AppointmentCreatePage() {
  const [startDate, setStartDate] = useState(''); // 2022-08-20 과 같은 형식이 들어옴 -> new Date()로 감싸서 사용 가능
  const [endDate, setEndDate] = useState('');

  return (
    <StyledContainer>
      <StyledContentContainer>
        <AppointmentCreateHeader />
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
      </StyledContentContainer>
    </StyledContainer>
  );
}

export default AppointmentCreatePage;
