import styled from '@emotion/styled';

const StyledMenuHeader = styled.div`
  font-size: 2.4rem;
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
  font-size: 2.4rem;
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
      border-radius: 1.2rem;
    `
  };

  &:hover {
    background: ${!isActive && theme.colors.TRANSPARENT_GRAY_100_80};
    border-radius: 1.2rem;
  } 
`
);

export { StyledMenuHeader, StyledContainer, StyledMenuIcon, StyledMenuTitle, StyledMenu };
