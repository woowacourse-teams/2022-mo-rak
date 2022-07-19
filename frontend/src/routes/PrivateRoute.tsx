import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { getSessionStorageItem } from '../utils/storage';

function PrivateRoute() {
  const token = getSessionStorageItem('token');

  return token ? <Outlet /> : <Navigate to="/" />;
}

export { PrivateRoute };
