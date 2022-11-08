import styled from '@emotion/styled';

const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
`;

const StyledTitle = styled.h1`
  font-size: 4rem;
  word-break: break-all;
`;

const StyledContent = styled.p`
  font-size: 2rem;
`;

const StyledLinkIcon = styled.img`
  width: 2.4rem;
`;

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

const StyledDescription = styled.div(
  ({ theme }) => `
  color: ${theme.colors.GRAY_400};
  font-size: 1.6rem;
`
);

export {
  StyledContainer,
  StyledTitle,
  StyledContent,
  StyledLinkIcon,
  StyledHelpIconContainer,
  StyledHelpIcon,
  StyledDescription
};
