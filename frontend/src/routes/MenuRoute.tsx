import React from 'react';
import { Outlet } from 'react-router-dom';
import { useMenuDispatchContext } from '../context/MenuProvider';

interface Props {
  menu: string;
}

function MenuRoute({ menu }: Props) {
  const dispatch = useMenuDispatchContext();
  dispatch({ type: 'SET_CLICKED_MENU', payload: menu });

  return <Outlet />;
}

export { MenuRoute };
