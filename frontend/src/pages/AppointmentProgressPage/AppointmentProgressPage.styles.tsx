import styled from '@emotion/styled';

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8rem;
`;

const StyledLoadingContainer = styled.div``;

const StyledLeftContainer = styled.div`
  display: flex;
  width: 45.2rem;
  flex-direction: column;
  gap: 1.8rem;
`;

const StyledRightContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
  align-items: center;
`;

export { StyledContainer, StyledLeftContainer, StyledRightContainer };
