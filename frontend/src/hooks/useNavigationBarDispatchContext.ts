import { useContext } from 'react';
import { NavigationBarDispatchContext } from '@/context/NavigationBarProvider';
import { CONTEXT_ERROR } from '@/constants/errorMessage';

function useNavigationBarDispatchContext() {
  const context = useContext(NavigationBarDispatchContext);

  if (!context) {
    throw new Error(CONTEXT_ERROR.NO_NAVIGATION_BAR_DISPATCH_CONTEXT);
  }

  return context;
}

export default useNavigationBarDispatchContext;
