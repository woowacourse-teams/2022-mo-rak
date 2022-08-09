import styled from '@emotion/styled';
import React from 'react';
import { AppointmentInterface } from '../../../types/appointment';

interface Props {
  title: AppointmentInterface['title'];
}

function AppointmentResultHeader({ title }: Props) {
  return (
    <>
      <StyledTitle>{title}</StyledTitle>
      <StyledContent>모락은 가장 많이 겹치는 시간을 추천해드립니다🦔</StyledContent>
    </>
  );
}

const StyledTitle = styled.h1`
  font-size: 4rem;
`;

const StyledContent = styled.p`
  font-size: 2.4rem;
`;

export default AppointmentResultHeader;
