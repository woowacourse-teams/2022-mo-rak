import React from 'react';
import styled from '@emotion/styled';

function AppointmentCreateHeader() {
  return (
    <>
      <StyledHeader>약속을 생성하세요</StyledHeader>
      <StyledHeaderContent>여러분이 만날날을 생성해주세요~</StyledHeaderContent>
    </>
  );
}

const StyledHeader = styled.header`
  font-size: 4rem;
`;

const StyledHeaderContent = styled.p`
  font-size: 2rem;
`;

export default AppointmentCreateHeader;
