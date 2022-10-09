import styled from '@emotion/styled';

const StyledGuideText = styled.div(
  ({ theme }) => `
  font-size: 2rem;
  color: ${theme.colors.GRAY_400};
`
);

const StyledSmallTitle = styled.h1`
  font-size: 2rem;
`;

export { StyledGuideText, StyledSmallTitle };
