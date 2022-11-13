import styled from '@emotion/styled';
import { Z_INDEX } from '@/constants/style';

const StyledContainer = styled.div<{ isVisible: boolean }>(
  ({ theme, isVisible }) => `
  z-index: ${Z_INDEX.MAX};
  display: ${isVisible ? 'flex' : 'none'};
  background-color: ${theme.colors.TRANSPARENT_BLACK_100_25};
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100%;
  backdrop-filter: blur(4px);
  
`
);

export { StyledContainer };
