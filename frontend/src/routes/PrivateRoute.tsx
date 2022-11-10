import { useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { getUser } from '@/apis/auth';
import useAuthDispatchContext from '@/hooks/useAuthDispatchContext';
import { getLocalStorageItem } from '@/utils/storage';

function PrivateRoute() {
  const token = getLocalStorageItem('token');
  const authDispatch = useAuthDispatchContext();

  useEffect(() => {
    (async () => {
      const res = await getUser();
      authDispatch({ type: 'SET_AUTH', payload: res.data });
    })();
  }, [token]);

  return token ? <Outlet /> : <Navigate to="/" />;
}

export { PrivateRoute };
