import styled from '@emotion/styled';

const StyledContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 3.2rem;
`;

const StyledTitle = styled.h1`
  font-size: 2.2rem;
  text-align: center;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-top: 1.2rem;
`;

const LottieWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const StyledGuide = styled.p(
  ({ theme }) => `
  text-align: center;
  font-size: 4rem;

  color: ${theme.colors.GRAY_400}
`
);
export { StyledContainer, StyledTitle, LottieWrapper, StyledGuide };
