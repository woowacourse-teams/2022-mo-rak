import styled from '@emotion/styled';

const StyledTitle = styled.h1(
  ({ theme }) => `
  color: ${theme.colors.BLACK_100};
  font-size: 3.2rem;
  word-break: break-all;
`
);

const StyledLoadingContainer = styled.div`
  height: 60rem;
`;

const StyledHelpIconContainer = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  width: 2rem;
  height: 2rem;
  background-color: ${theme.colors.GRAY_300};
  border-radius: 100%;
`
);

const StyledHelpIcon = styled.img`
  width: 1.6rem;
`;

const StyledDescription = styled.div(
  ({ theme }) => `
  color: ${theme.colors.GRAY_400};
  font-size: 1.6rem;
`
);

export {
  StyledTitle,
  StyledLoadingContainer,
  StyledHelpIconContainer,
  StyledHelpIcon,
  StyledDescription
};
