import { useState } from 'react';

function useModal() {
  const [isVisible, setIsVisible] = useState(false);

  const open = () => {
    setIsVisible(true);
  };

  const close = () => {
    setIsVisible(false);
  };

  const toggle = () => {
    setIsVisible((prev) => !prev);
  };

  return [isVisible, open, close, toggle] as const;
}

export default useModal;
