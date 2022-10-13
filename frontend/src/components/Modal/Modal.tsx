import { MouseEvent, PropsWithChildren, useRef } from 'react';
import { StyledModalContainer } from './Modal.styles';

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

export default Modal;
