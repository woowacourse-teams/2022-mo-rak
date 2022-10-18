import styled from '@emotion/styled';

const StyledContainer = styled.div(
  ({ theme }) => `
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: fixed;
  top: 0;
  padding-left: 2rem;
  z-index: 1;
  width: 100%;
  height: 10%;
  background:${theme.colors.WHITE_100};
`
);

const StyledLogo = styled.img`
  width: 10rem;
  cursor: pointer;
`;

const StyledGroupFirstCharacter = styled.div(
  ({ theme }) => `
  color: ${theme.colors.WHITE_100};
  font-size: 4.8rem;
`
);

const StyledProfileContainer = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  align-items: center;
  width: 8rem;
  height: 8rem;
  border-radius: 1.2rem;
  font-family: 'Nanum Gothic', sans-serif;
  background: ${theme.colors.PURPLE_50};
`
);

const StyledName = styled.div`
  font-size: 2rem;
`;

const StyledCurrentGroupContainer = styled.div`
  display: flex;
  gap: 2rem;
  align-items: center;
  padding-right: 2rem;
`;

export {
  StyledContainer,
  StyledLogo,
  StyledGroupFirstCharacter,
  StyledProfileContainer,
  StyledName,
  StyledCurrentGroupContainer
};
