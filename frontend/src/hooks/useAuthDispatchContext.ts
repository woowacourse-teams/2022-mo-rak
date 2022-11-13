import { useContext } from 'react';
import { AuthDispatchContext } from '@/context/AuthProvider';
import { CONTEXT_ERROR } from '@/constants/errorMessage';

function useAuthDispatchContext() {
  const context = useContext(AuthDispatchContext);

  if (!context) {
    throw new Error(CONTEXT_ERROR.NO_AUTH_DISPATCH_CONTEXT);
  }

  return context;
}

export default useAuthDispatchContext;
