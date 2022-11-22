import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50%;
  background-color: ${theme.colors.YELLOW_50};
  flex-direction: column;
  gap: 6.8rem;
`
);

export { StyledContainer };
