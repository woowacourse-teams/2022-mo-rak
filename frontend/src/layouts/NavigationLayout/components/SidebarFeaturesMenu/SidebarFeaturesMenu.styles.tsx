import styled from '@emotion/styled';

const StyledMenuHeader = styled.div`
  font-size: 1.6rem;
  text-align: left;
  margin-bottom: 2rem;
`;

const StyledContainer = styled.div`
  margin: 2.8rem 0;
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
      background-color: ${theme.colors.GRAY_100}; 
      border-top-left-radius: 4rem; 
      border-bottom-left-radius: 4rem;
    `
  };

  &:hover {
    background-color: ${!isActive && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

export { StyledMenuHeader, StyledContainer, StyledMenuIcon, StyledMenuTitle, StyledMenu };
