import styled from '@emotion/styled';

const StyledTitle = styled.div(
  ({ theme }) => `
  font-size: 2.4rem;
  color: ${theme.colors.PURPLE_100};
`
);

const StyledContent = styled.p`
  font-size: 2.2rem;
`;

const StyledLabel = styled.label`
  font-size: 2.2rem;
`;

export { StyledTitle, StyledContent, StyledLabel };
