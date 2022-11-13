import { useContext } from 'react';
import { NavigationBarContext } from '@/contexts/NavigationBarProvider';

function useNavigationBarContext() {
  const context = useContext(NavigationBarContext);

  if (!context) {
    throw new Error('NavigationBarProvider를 찾을 수 없습니다.');
  }

  return context;
}

export default useNavigationBarContext;
