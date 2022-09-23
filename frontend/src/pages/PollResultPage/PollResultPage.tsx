import styled from '@emotion/styled';

import PollResultContainer from '../../components/PollResult/PollResultContainer/PollResultContainer';

function PollResultPage() {
  return (
    <StyledContainer>
      <PollResultContainer />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export default PollResultPage;
