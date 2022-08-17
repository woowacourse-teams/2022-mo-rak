import React, { createContext, useContext, useReducer, PropsWithChildren, Dispatch } from 'react';

interface MenuState {
  clickedMenu: string;
  isVisibleGroups: boolean;
}

interface Menu {
  type: 'SET_CLICKED_MENU' | 'SET_SHOW_GROUP_LIST';
  payload: any; // TODO: any 변경하기
}

const initialState = {
  clickedMenu: 'poll',
  isVisibleGroups: false
};

const MenuContext = createContext<MenuState | undefined>(undefined);
const MenuDispatchContext = createContext<Dispatch<Menu> | undefined>(undefined);

function menuReducer(state: MenuState, action: Menu) {
  switch (action.type) {
    case 'SET_CLICKED_MENU':
      return {
        ...state,
        clickedMenu: action.payload
      };
    case 'SET_SHOW_GROUP_LIST':
      return {
        ...state,
        isVisibleGroups: action.payload
      };
    default:
      return state;
  }
}

function MenuProvider({ children }: PropsWithChildren) {
  const [state, dispatch] = useReducer(menuReducer, initialState);

  return (
    <MenuContext.Provider value={state}>
      <MenuDispatchContext.Provider value={dispatch}>
        {children}
      </MenuDispatchContext.Provider>
    </MenuContext.Provider>
  );
}

// TODO: hook으로 빼주자
function useMenuContext() {
  const context = useContext(MenuContext);

  if (!context) {
    throw new Error('MenuProvider를 찾을 수 없습니다.');
  }

  return context;
}

function useMenuDispatchContext() {
  const context = useContext(MenuDispatchContext);

  if (!context) {
    throw new Error('MenuProvider를 찾을 수 없습니다.');
  }

  return context;
}

export { useMenuContext, useMenuDispatchContext, MenuProvider };
