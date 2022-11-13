import styled from '@emotion/styled';
import { Z_INDEX } from '@/constants/style';

const StyledContainer = styled.div<{ isVisible: boolean }>(
  ({ isVisible, theme }) => `
  display: ${isVisible ? 'flex' : 'none'};
  flex-direction: column;
  position: absolute;
  top: 0;
  width: 100%;
  min-height: 100%;
  z-index: ${Z_INDEX.DRAWER}; 
  background: ${theme.colors.WHITE_100};
  padding: 4rem;
  gap: 2rem;
`
);

const StyledCloseButton = styled.button`
  position: absolute;
  top: 4rem;
  right: 4rem;
`;

export { StyledContainer, StyledCloseButton };
