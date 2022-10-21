import { useContext } from 'react';
import { AuthDispatchContext } from '../context/AuthProvider';

function useAuthDispatchContext() {
  const context = useContext(AuthDispatchContext);

  if (!context) {
    throw new Error('AuthDispatchContext를 찾을 수 없습니다.');
  }

  return context;
}

export default useAuthDispatchContext;
