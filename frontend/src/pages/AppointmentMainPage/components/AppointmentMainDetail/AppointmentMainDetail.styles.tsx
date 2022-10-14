import styled from '@emotion/styled';

const StyledCloseTime = styled.p`
  font-size: 1.4rem;
  align-self: end;
`;

const StyledDetail = styled.span(
  ({ theme }) => `
  color: ${theme.colors.PURPLE_100};
  font-size: 1.2rem;
`
);

export { StyledCloseTime, StyledDetail };