import { useContext } from 'react';
import { MenuContext } from '../context/MenuProvider';

function useMenuContext() {
  const context = useContext(MenuContext);

  if (!context) {
    throw new Error('MenuProvider를 찾을 수 없습니다.');
  }

  return context;
}

export default useMenuContext;
