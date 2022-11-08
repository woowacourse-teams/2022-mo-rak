import styled from '@emotion/styled';

const StyledHeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const StyledHeader = styled.header`
  font-size: 4.8rem;
  word-break: break-all;
`;

const StyledDescription = styled.p`
  font-size: 2.4rem;
  padding: 0.4rem 0;
  overflow-y: auto;
`;

export { StyledHeaderContainer, StyledHeader, StyledDescription };
