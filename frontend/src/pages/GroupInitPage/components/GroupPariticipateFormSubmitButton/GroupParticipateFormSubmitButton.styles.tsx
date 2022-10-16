import styled from '@emotion/styled';

const StyledButton = styled.button(
  ({ theme }) => `
  background-color: ${theme.colors.YELLOW_200};
  color: ${theme.colors.WHITE_100};
  border-radius: 0 10px 10px 0;
  fontSize: 1.6rem;
  padding: 2.8rem 0; 
  width: 20vw;
  max-width: 21.2rem;
  font-size: 2.4rem;

  &: hover {
    background-color: ${theme.colors.YELLOW_300};
    transition: all 0.2s linear;
  }
`
);

export { StyledButton };
