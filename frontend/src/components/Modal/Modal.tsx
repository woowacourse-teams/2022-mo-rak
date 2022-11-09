import { MouseEvent, PropsWithChildren, useRef } from 'react';
import { StyledContainer } from '@/components/Modal/Modal.styles';

type Props = {
  isVisible: boolean;
  close: () => void;
} & PropsWithChildren;

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
