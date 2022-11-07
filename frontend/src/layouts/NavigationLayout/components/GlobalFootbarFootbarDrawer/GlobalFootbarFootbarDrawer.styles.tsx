import styled from '@emotion/styled';

const StyledContainer = styled.div<{ isVisible: boolean }>(
  ({ isVisible, theme }) => `
  display: ${isVisible ? 'flex' : 'none'};
  flex-direction: column;
  position: absolute;
  top: 0;
  width: 100%;
  min-height: 100%;
  z-index: 999; 
  background-color: ${theme.colors.WHITE_100};
  padding: 4rem;
  gap: 2rem;
`
);

const StyledCloseButton = styled.button`
  position: absolute;
  top: 4rem;
  right: 4rem;
  z-index: 1;
`;

export { StyledContainer, StyledCloseButton };
