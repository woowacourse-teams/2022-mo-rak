import styled from '@emotion/styled';

const StyledDetail = styled.span(
  ({ theme }) => `
  color: ${theme.colors.PURPLE_100};
  font-size: 1.6rem;
`
);

const StyledRolesContainer = styled.div`
  width: 100%;
  display: flex;
  gap: 2rem;
  overflow-x: auto;
`;

const StyledRoleContainer = styled.div`
  flex-shrink: 0;
`;

const StyledLottieContainer = styled.div(
  ({ isVisible }: { isVisible: boolean }) => `
  display: ${isVisible ? 'flex' : 'none'};
  position: absolute;
  justify-content: center;
  align-items: center;
  `
);

export { StyledDetail, StyledRolesContainer, StyledRoleContainer, StyledLottieContainer };
