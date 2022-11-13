import { useContext } from 'react';
import { AuthContext } from '@/context/AuthProvider';
import { CONTEXT_ERROR } from '@/constants/errorMessage';

function useAuthContext() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error(CONTEXT_ERROR.NO_AUTH_CONTEXT);
  }

  return context;
}

export default useAuthContext;
