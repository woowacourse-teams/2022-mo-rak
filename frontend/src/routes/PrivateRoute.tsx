import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { getLocalStorageItem } from '../utils/storage';

// TODO: 폴더 이름 routes 맞을까? 이곳에 넣어주는 게 맞을까?
function PrivateRoute() {
  const token = getLocalStorageItem('token');

  return token ? <Outlet /> : <Navigate to="/" />;
}

export { PrivateRoute };
