import styled from '@emotion/styled';

const StyledTitle = styled.h1(
  ({ theme }) => `
  color: ${theme.colors.BLACK_100};
  font-size: 3.2rem;
`
);

const StyledLoadingContainer = styled.div`
  height: 60rem;
`;

export { StyledTitle, StyledLoadingContainer };
