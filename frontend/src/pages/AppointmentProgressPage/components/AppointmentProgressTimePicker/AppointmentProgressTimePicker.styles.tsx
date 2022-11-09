import styled from '@emotion/styled';

const StyledTime = styled.div<{ isSelected: boolean }>(
  ({ theme, isSelected }) => `
  width: 100%;
  font-size: 2rem;
  text-align: center;
  padding: 2rem 0;
  border-radius: 10px;
  cursor: pointer;

  background-color: ${
    isSelected ? theme.colors.YELLOW_100 : theme.colors.TRANSPARENT_YELLOW_100_33
  };
  :hover {
    background-color: ${theme.colors.YELLOW_100};
  }
`
);

const StyledGuide = styled.p(
  ({ theme }) => `
  font-size: 2rem;
  text-align: center;
  line-height: 44.8rem;

  color: ${theme.colors.GRAY_400};
`
);
export { StyledTime, StyledGuide };
