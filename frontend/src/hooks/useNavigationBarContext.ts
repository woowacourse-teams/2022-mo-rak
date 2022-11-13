import { useContext } from 'react';
import { NavigationBarContext } from '@/context/NavigationBarProvider';
import { CONTEXT_ERROR } from '@/constants/errorMessage';

function useNavigationBarContext() {
  const context = useContext(NavigationBarContext);

  if (!context) {
    throw new Error(CONTEXT_ERROR.NO_NAVIGATION_BAR_CONTEXT);
  }

  return context;
}

export default useNavigationBarContext;
