import styled from '@emotion/styled';

const StyledContainer = styled.div`
  height: 100vh;
  width: 100vw;
`;

const StyledTopContainer = styled.div`
  height: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  align-items: center;
`;

const StyledBottomContainer = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50%;
  background: ${theme.colors.YELLOW_50};
  flex-direction: column;
  gap: 6.8rem;
`
);

const StyledLogo = styled.img`
  width: 22rem;
  margin-bottom: 4rem;
`;

const StyledBigText = styled.div`
  font-size: 4.4rem;
  margin-bottom: 2rem;
`;

const StyledSmallText = styled.div`
  font-size: 2.4rem;
  padding: 0 4rem;
  text-align: center;
  line-height: 3.2rem;
`;

export {
  StyledContainer,
  StyledTopContainer,
  StyledBottomContainer,
  StyledLogo,
  StyledBigText,
  StyledSmallText
};
