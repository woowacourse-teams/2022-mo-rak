import { MouseEvent, PropsWithChildren, useRef } from 'react';
import { StyledContainer } from './Modal.styles';

interface Props extends PropsWithChildren {
  isVisible: boolean;
  close: () => void;
}

function Modal({ children, isVisible, close }: Props) {
  const outsideRef = useRef<HTMLDivElement>(null);

  const handleCloseModal = (e: MouseEvent<HTMLDivElement>) => {
    if (outsideRef.current === e.target) {
      close();
    }
  };

  return (
    <StyledContainer
      ref={outsideRef}
      isVisible={isVisible}
      tabIndex={-1}
      onClick={handleCloseModal}
    >
      {children}
    </StyledContainer>
  );
}

export default Modal;
