import styled from '@emotion/styled';
import { useState } from 'react';
import AppointmentCreateForm from '../../components/AppointmentCreate/AppointmentCreateForm/AppointmentCreateForm';
import Calendar from '../../components/@common/Calendar/Calendar';
import AppointmentCreateHeader from '../../components/AppointmentCreate/AppointmentCreateHeader/AppointmentCreateHeadert';

function AppointmentCreatePage() {
  const [startDate, setStartDate] = useState(''); // 2022-08-20 과 같은 형식이 들어옴 -> new Date()로 감싸서 사용 가능
  const [endDate, setEndDate] = useState('');

  return (
    <StyledContainer>
      <StyledLeftContainer>
        <AppointmentCreateHeader />
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
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6rem;
`;

const StyledLeftContainer = styled.div`
  display: flex;
  width: 45.2rem;
  flex-direction: column;
  gap: 1.8rem;
`;

const StyledRightContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4rem;
`;

export default AppointmentCreatePage;
