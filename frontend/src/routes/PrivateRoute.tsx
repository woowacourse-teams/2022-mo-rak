import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { getLocalStorageItem } from '../utils/storage';

function PrivateRoute() {
  const token = getLocalStorageItem('token');

  return token ? <Outlet /> : <Navigate to="/" />;
}

export { PrivateRoute };
