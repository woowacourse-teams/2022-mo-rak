import { useContext } from 'react';

import { NavigationBarDispatchContext } from '@/context/NavigationBarProvider';

function useNavigationBarDispatchContext() {
  const context = useContext(NavigationBarDispatchContext);

  if (!context) {
    throw new Error('NavigationBarProvider를 찾을 수 없습니다.');
  }

  return context;
}

export default useNavigationBarDispatchContext;
