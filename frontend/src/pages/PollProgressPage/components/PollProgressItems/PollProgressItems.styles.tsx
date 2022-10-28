import styled from '@emotion/styled';

const StyledDescription = styled.div<{ isVisible: boolean }>(
  ({ isVisible }) => `
  display: ${isVisible ? 'block' : 'none'};
`
);

export { StyledDescription };
