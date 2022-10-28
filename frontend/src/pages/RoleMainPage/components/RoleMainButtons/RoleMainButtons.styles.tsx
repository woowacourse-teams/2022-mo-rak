import styled from '@emotion/styled';

const StyledAnimationContainer = styled.div(
  ({ isVisible }: { isVisible: boolean }) => `
  display: ${isVisible ? 'flex' : 'none'};
  position: absolute;
  justify-content: center;
  align-items: center;
  `
);

export { StyledAnimationContainer };
