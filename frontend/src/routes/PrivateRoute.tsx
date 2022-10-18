import { useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { getUser } from '../api/auth';
import useAuthDispatchContext from '../hooks/useAuthDispatchContext';
import { getLocalStorageItem } from '../utils/storage';

function PrivateRoute() {
  const token = getLocalStorageItem('token');
  const dispatch = useAuthDispatchContext();

  useEffect(() => {
    (async () => {
      const res = await getUser();
      dispatch({ type: 'SET_AUTH', payload: res.data });
    })();
  }, [token]);

  return token ? <Outlet /> : <Navigate to="/" />;
}

export { PrivateRoute };
