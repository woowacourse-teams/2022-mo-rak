import { useState } from 'react';
import AppointmentCreateForm from './components/AppointmentCreateForm/AppointmentCreateForm';
import Calendar from '../../components/Calendar/Calendar';
import AppointmentCreateHeader from './components/AppointmentCreateHeader/AppointmentCreateHeader';
import FlexContainer from '../../components/FlexContainer/FlexContainer';
import {
  StyledContainer,
  StyledLeftContainer,
  StyledRightContainer
} from './AppointmentCreatePage.styles';

function AppointmentCreatePage() {
  const [startDate, setStartDate] = useState(''); // 2022-08-20 과 같은 형식이 들어옴 -> new Date()로 감싸서 사용 가능
  const [endDate, setEndDate] = useState('');

  return (
    <StyledContainer>
      <FlexContainer flexDirection="column" gap="2rem">
        <AppointmentCreateHeader />
        <FlexContainer gap="4rem">
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
      </FlexContainer>
    </StyledContainer>
  );
}

export default AppointmentCreatePage;
