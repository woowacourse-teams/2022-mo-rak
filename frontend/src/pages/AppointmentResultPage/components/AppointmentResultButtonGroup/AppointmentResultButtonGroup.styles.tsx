import styled from '@emotion/styled';

const StyledHelpIconContainer = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  width: 2.8rem;
  height: 2.8rem;
  background: ${theme.colors.GRAY_300};
  border-radius: 100%;
`
);

const StyledHelpIcon = styled.img`
  width: 2rem;
`;

export { StyledHelpIconContainer, StyledHelpIcon };
