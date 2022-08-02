import styled from '@emotion/styled';
import React, { useState } from 'react';
import AppointmentCreateForm from '../../components/AppointmentCreate/AppointmentCreateForm/AppointmentCreateForm';
import Calendar from '../../components/common/Calendar/Calendar';
import AppointmentCreateHeader from '../../components/AppointmentCreate/AppointmentCreateHeader/AppointmentCreateHeadert';

function AppointmentCreatePage() {
  // TODO: 이 state들을 Form에 props로 내려주는 게 맞는 구조일지 생각해보기
  // NOTE: 현재는 임시적으로 Page에서 state를 만들어서 props로 전달해줌.
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
        {/* TODO: 이렇게 Form에 props로 내려주는 게 맞는 구조일지 생각해보기 */}
        <AppointmentCreateForm startDate={startDate} endDate={endDate} />
      </StyledRightContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  width: calc(100% - 36.4rem);
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
