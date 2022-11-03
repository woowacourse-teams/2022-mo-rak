import { useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import useNavigationBarDispatchContext from '@/hooks/useNavigationBarDispatchContext';
import { Menu } from '@/types/menu';

type Props = {
  menu: Menu;
};

function MenuRoute({ menu }: Props) {
  const navigationBarDispatch = useNavigationBarDispatchContext();

  useEffect(() => {
    navigationBarDispatch({ type: 'SET_ACTIVE_MENU', payload: menu });
  }, [menu]);

  return <Outlet />;
}

export { MenuRoute };
