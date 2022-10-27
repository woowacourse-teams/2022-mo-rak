import { useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import useMenuDispatchContext from '../hooks/useMenuDispatchContext';
import { Menu } from '../types/menu';

type Props = {
  menu: Menu;
};

function MenuRoute({ menu }: Props) {
  const menuDispatch = useMenuDispatchContext();

  useEffect(() => {
    menuDispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
  }, [menu]);

  return <Outlet />;
}

export { MenuRoute };
