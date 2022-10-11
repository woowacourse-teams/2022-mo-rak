import styled from '@emotion/styled';

const StyledHeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-height: 20.8rem;
`;

const StyledHeader = styled.header`
  font-size: 4.8rem;
`;

const StyledDescription = styled.div`
  font-size: 2.4rem;
  overflow-y: auto;
`;

export { StyledHeaderContainer, StyledHeader, StyledDescription };
