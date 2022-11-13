import styled from '@emotion/styled';
import { Z_INDEX } from '@/constants/style';

const StyledContainer = styled.div(
  ({ theme }) => `
  position: fixed;
  top: 0;
  width: 100vw;
  height: 10vh;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-left: 2rem;
  z-index: ${Z_INDEX.GLOBALBAR_FOOTBAR}; 
  background:${theme.colors.WHITE_100};
`
);

const StyledLogo = styled.img`
  width: 8rem;
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
  background-color: ${theme.colors.PURPLE_50};
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
