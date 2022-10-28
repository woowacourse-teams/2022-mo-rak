import styled from '@emotion/styled';

const StyledTitle = styled.div(
  ({ theme }) => `
  font-size: 2.8rem;
  color: ${theme.colors.PURPLE_100};
`
);

const StyledContent = styled.p`
  font-size: 2.8rem;
`;

const StyledLabel = styled.label`
  font-size: 2.8rem;
`;

export { StyledTitle, StyledContent, StyledLabel };
