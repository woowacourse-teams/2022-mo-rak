import { MouseEvent, PropsWithChildren, useRef } from 'react';
import styled from '@emotion/styled';

interface Props extends PropsWithChildren {
  isVisible: boolean;
  close: () => void;
}

function Modal({ children, isVisible, close }: Props) {
  const outside = useRef<HTMLDivElement>(null);

  const handleCloseModal = (e: MouseEvent<HTMLDivElement>) => {
    if (outside.current === e.target) {
      close();
    }
  };

  return (
    <StyledModalContainer
      ref={outside}
      isVisible={isVisible}
      tabIndex={-1}
      onClick={handleCloseModal}
    >
      {children}
    </StyledModalContainer>
  );
}

const StyledModalContainer = styled.div<{ isVisible: boolean }>(
  ({ theme, isVisible }) => `
  z-index: 10;
  display: ${isVisible ? 'flex' : 'none'};
  background-color: ${theme.colors.TRANSPARENT_BLACK_100_25};
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100vw;
`
);

export default Modal;
