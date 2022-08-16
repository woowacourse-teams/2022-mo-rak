import React, { createContext, useContext, useReducer, PropsWithChildren, Dispatch } from 'react';

interface MenuState {
  clickedMenu: string;
  isVisibleGroupList: boolean;
}

interface MenuAction {
  type: 'SET_CLICKED_MENU' | 'SET_SHOW_GROUP_LIST';
  menu?: string;
  isVisible?: boolean;
}

const initialState = {
  clickedMenu: 'poll',
  isVisibleGroupList: false
};

const MenuStateContext = createContext<MenuState | undefined>(undefined);
const MenuDispatchContext = createContext<Dispatch<MenuAction> | undefined>(undefined);

function menuReducer(state: MenuState, action: MenuAction) {
  switch (action.type) {
    case 'SET_CLICKED_MENU':
      return {
        ...state,
        clickedMenu: action.menu as string
      };
    case 'SET_SHOW_GROUP_LIST':
      return {
        ...state,
        isVisibleGroupList: action.isVisible as boolean
      };
    default:
      return state;
  }
}

function MenuProvider({ children }: PropsWithChildren) {
  const [state, dispatch] = useReducer(menuReducer, initialState);

  return (
    <MenuStateContext.Provider value={state}>
      <MenuDispatchContext.Provider value={dispatch}>
        {children}
      </MenuDispatchContext.Provider>
    </MenuStateContext.Provider>
  );
}

function useMenuState() {
  const context = useContext(MenuStateContext);

  if (!context) {
    throw new Error('MenuProvider를 찾을 수 없습니다.');
  }

  return context;
}

function useMenuDispatch() {
  const context = useContext(MenuDispatchContext);

  if (!context) {
    throw new Error('MenuProvider를 찾을 수 없습니다.');
  }

  return context;
}

export { useMenuState, useMenuDispatch, MenuProvider };
