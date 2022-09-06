import styled from '@emotion/styled';

import { AppointmentInterface } from '../../../types/appointment';

interface Props {
  title: AppointmentInterface['title'];
  description: AppointmentInterface['description'];
}

function AppointmentProgressHeader({ title, description }: Props) {
  return (
    <StyledHeaderContainer>
      <StyledHeader>{title}</StyledHeader>
      <StyledDescription>{description}</StyledDescription>
    </StyledHeaderContainer>
  );
}

const StyledHeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 3.6rem;
  max-height: 20.8rem;
  /* min-height: 20.8rem; */
`;

const StyledHeader = styled.header`
  font-size: 4.8rem;
`;

const StyledDescription = styled.div`
  font-size: 2rem;
  overflow-y: auto;
`;

export default AppointmentProgressHeader;
