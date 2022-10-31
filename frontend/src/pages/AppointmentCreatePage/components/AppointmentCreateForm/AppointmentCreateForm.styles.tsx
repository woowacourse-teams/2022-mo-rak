import styled from '@emotion/styled';

const StyledForm = styled.form<{
  isMobile: boolean;
}>(
  ({ isMobile }) => `
  width: ${isMobile ? '52rem' : '66rem'};
  display: flex;
  flex-direction: column;
  gap: 3.2rem;
`
);

export { StyledForm };
