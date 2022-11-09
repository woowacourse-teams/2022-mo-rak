import { useContext } from 'react';
import { AuthContext } from '@/context/AuthProvider';

function useAuthContext() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('AuthContext를 찾을 수 없습니다.');
  }

  return context;
}

export default useAuthContext;
