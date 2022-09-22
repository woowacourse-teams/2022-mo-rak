import styled from '@emotion/styled';

import PollCreateForm from '../../components/PollCreate/PollCreateForm/PollCreateForm';

function PollCreatePage() {
  return (
    <StyledContainer>
      <PollCreateForm />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
  /* TODO: 위니랑 이야기해보기 */
  /* margin: 8rem 0;
  height: 100%; */
`;

export default PollCreatePage;
