import { useContext } from 'react';
import { MenuDispatchContext } from '../context/MenuProvider';

function useMenuDispatchContext() {
  const context = useContext(MenuDispatchContext);

  if (!context) {
    throw new Error('MenuProvider를 찾을 수 없습니다.');
  }

  return context;
}

export default useMenuDispatchContext;
