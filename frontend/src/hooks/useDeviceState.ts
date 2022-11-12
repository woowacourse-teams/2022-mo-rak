import { useEffect, useState } from 'react';

function getIsMobile() {
  if (window.innerWidth < 500) {
    return true;
  }
  return false;
}

function useDeviceState() {
  const [isMobile, setIsMobile] = useState(getIsMobile());

  useEffect(() => {
    function handleResize() {
      setIsMobile(getIsMobile());
    }

    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return isMobile;
}

export default useDeviceState;
