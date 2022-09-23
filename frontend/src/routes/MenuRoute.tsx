import { Outlet } from 'react-router-dom';
import useMenuDispatchContext from '../hooks/useMenuDispatchContext';
import {Menu} from '../types/menu';

interface Props {
  // TODO: 존재하는 메뉴 literal
  menu: Menu;
}

function MenuRoute({ menu }: Props) {
  const dispatch = useMenuDispatchContext();
  dispatch({ type: 'SET_ACTIVE_MENU', payload: menu });

  return <Outlet />;
}

export { MenuRoute };
