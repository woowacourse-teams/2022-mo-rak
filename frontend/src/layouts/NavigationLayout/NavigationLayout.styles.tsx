import styled from '@emotion/styled';

const StyledContainer = styled.div<{
  isMobile: boolean;
}>(
  ({ isMobile }) => `
  display: ${isMobile ? 'block' : 'flex'};
`
);

export { StyledContainer };
