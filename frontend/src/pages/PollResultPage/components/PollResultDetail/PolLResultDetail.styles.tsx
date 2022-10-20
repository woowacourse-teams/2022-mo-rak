import styled from '@emotion/styled';

const StyledContainer = styled.div`
  display: flex;
  gap: 1.2rem;
  position: relative;
`;

const StyledCloseTime = styled.p`
  font-size: 1.2rem;
  position: absolute;
  right: 0;
`;

const StyledDetail = styled.span(
  ({ theme }) => `
  color: ${theme.colors.PURPLE_100};
  font-size: 1.2rem;
`
);

export { StyledContainer, StyledCloseTime, StyledDetail };
