import styled from '@emotion/styled';

const StyledMenuHeader = styled.div`
  font-size: 1.6rem;
  text-align: left;
  padding: 2rem 0;
`;

const StyledContainer = styled.div`
  height: 25%;
`;

const StyledMenuIcon = styled.img`
  width: 2.4rem;
`;

const StyledMenuTitle = styled.div`
  font-size: 1.6rem;
`;

const StyledMenu = styled.div<{
  isActive: boolean;
}>(
  ({ isActive, theme }) => `
    display: flex;
    gap: 2rem;
    align-items: center;
    cursor: pointer;
    padding: 2rem;

  ${
    isActive &&
    `
      background: ${theme.colors.GRAY_100}; 
      border-top-left-radius: 4rem; 
      border-bottom-left-radius: 4rem;
    `
  };

  &:hover {
    background: ${!isActive && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

export { StyledMenuHeader, StyledContainer, StyledMenuIcon, StyledMenuTitle, StyledMenu };
