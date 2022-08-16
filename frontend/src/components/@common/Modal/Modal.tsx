import React, { MouseEvent, PropsWithChildren, useRef } from 'react';
import styled from '@emotion/styled';

interface Props extends PropsWithChildren {
  visible: boolean;
  onClose: () => void;
}

function Modal({ children, visible, onClose }: Props) {
  const outside = useRef<HTMLDivElement>(null);

  const handleCloseModal = (e: MouseEvent<HTMLDivElement>) => {
    if (outside.current === e.target) {
      onClose();
    }
  };

  return (
    <StyledModalContainer
      ref={outside}
      visible={visible}
      tabIndex={-1}
      onClick={handleCloseModal}
    >
      {children}
    </StyledModalContainer>
  );
}

const StyledModalContainer = styled.div<{visible: boolean}>(({ theme, visible }) => `
  z-index: 10;
  display: ${visible ? 'flex' : 'none'};
  background-color: ${theme.colors.TRANSPARENT_BLACK_100_25};
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100vw;
`);

export default Modal;
