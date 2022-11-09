import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ theme }) => `
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
  width: 78rem; 
  height: 59.6rem;
  overflow-y: auto;
  border-radius: 15px;
  padding: 2rem;
  
  background-color: ${theme.colors.WHITE_100};
  box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_100_25};
`
);

const StyledResultText = styled.span`
  font-size: 2rem;
`;

const StyledCrownIcon = styled.img`
  width: 2rem;
`;

const StyledRank = styled.div<{
  isClicked: boolean;
}>(
  // NOTE: 아래처럼 변수 사용하는 것은 밑으로 빼줘도 괜찮을듯?
  ({ theme, isClicked }) => `
  text-align: center;
  width: 100%;
  border-radius: 0.8rem;
  padding: 2rem 4.4rem;
  cursor: pointer;
  border: 0.1rem solid ${theme.colors.TRANSPARENT_BLACK_100_25};
  box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_100_25};
  background-color: ${isClicked ? theme.colors.PURPLE_100 : theme.colors.WHITE_100};
  color: ${isClicked ? theme.colors.WHITE_100 : theme.colors.BLACK_100};
`
);

export { StyledContainer, StyledResultText, StyledCrownIcon, StyledRank };
