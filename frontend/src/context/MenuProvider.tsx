import { createContext, useReducer, PropsWithChildren, Dispatch } from 'react';
import { Menu } from '../types/menu';
interface MenuState {
  activeMenu: Menu;
  isVisibleListMenu: boolean;
  isVisibleGroupsModal: boolean;
}

interface SetActiveMenuAction {
  type: 'SET_ACTIVE_MENU';
  payload: Menu;
}

interface SetIsVisibleGroupsModalAction {
  type: 'SET_IS_VISIBLE_GROUPS_MODAL';
  payload: boolean;
}

interface SetIsVisibleMenuList {
  type: 'SET_IS_VISIBLE_LIST_MENU';
  payload: boolean;
}

interface ToggleGroupsModalAction {
  type: 'TOGGLE_GROUPS_MODAL';
}

type MenuAction =
  | SetActiveMenuAction
  | SetIsVisibleGroupsModalAction
  | ToggleGroupsModalAction
  | SetIsVisibleMenuList;

const initialState = {
  activeMenu: 'poll',
  isVisibleListMenu: false,
  isVisibleGroupsModal: false
} as const;

const MenuContext = createContext<MenuState | null>(null);
const MenuDispatchContext = createContext<Dispatch<MenuAction> | null>(null);

function menuReducer(state: MenuState, action: MenuAction): MenuState {
  switch (action.type) {
    case 'SET_ACTIVE_MENU':
      return {
        ...state,
        activeMenu: action.payload
      };
    case 'SET_IS_VISIBLE_GROUPS_MODAL':
      return {
        ...state,
        isVisibleGroupsModal: action.payload
      };
    case 'TOGGLE_GROUPS_MODAL':
      return {
        ...state,
        isVisibleGroupsModal: !state.isVisibleGroupsModal
      };
    case 'SET_IS_VISIBLE_LIST_MENU':
      return {
        ...state,
        isVisibleListMenu: action.payload
      };
    default:
      return state;
  }
}

function MenuProvider({ children }: PropsWithChildren) {
  const [state, dispatch] = useReducer(menuReducer, initialState);

  return (
    <MenuContext.Provider value={state}>
      <MenuDispatchContext.Provider value={dispatch}>{children}</MenuDispatchContext.Provider>
    </MenuContext.Provider>
  );
}

export { MenuContext, MenuDispatchContext, MenuProvider };
