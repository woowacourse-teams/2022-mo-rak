import styled from '@emotion/styled';

const StyledContainer = styled.div`
  display: flex;
  gap: 2rem;
  overflow-x: auto;
`;

const StyledRoleContainer = styled.div`
  flex-shrink: 0;
`;

const StyledName = styled.span(
  ({ theme }) => `
  color: ${theme.colors.PURPLE_100};
  font-size: 1.6rem;
`
);

export { StyledName, StyledContainer, StyledRoleContainer };
