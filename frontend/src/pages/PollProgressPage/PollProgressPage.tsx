import styled from '@emotion/styled';

import PollProgressForm from './components/PollProgressForm/PollProgressForm';

function PollProgressPage() {
  return (
    <StyledContainer>
      <PollProgressForm />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default PollProgressPage;
