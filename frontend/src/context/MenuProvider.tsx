import { createContext, useReducer, PropsWithChildren, Dispatch } from 'react';
import { Menu } from '../types/menu';

type MenuState = {
  activeMenu: Menu;
  isDrawerVisible: boolean;
  isGroupsModalVisible: boolean;
};

type SetActiveMenuAction = {
  type: 'SET_ACTIVE_MENU';
  payload: Menu;
};

type SetIsGroupsModalVisibleAction = {
  type: 'SET_IS_GROUPS_MODAL_VISIBLE';
  payload: boolean;
};

type SetIsDrawerVisibleAction = {
  type: 'SET_IS_DRAWER_VISIBLE';
  payload: boolean;
};

type ToggleGroupsModalAction = {
  type: 'TOGGLE_GROUPS_MODAL';
};

type MenuAction =
  | SetActiveMenuAction
  | SetIsGroupsModalVisibleAction
  | ToggleGroupsModalAction
  | SetIsDrawerVisibleAction;

const initialState = {
  activeMenu: 'poll',
  isDrawerVisible: false,
  isGroupsModalVisible: false
} as const;

function menuReducer(state: MenuState, action: MenuAction): MenuState {
  switch (action.type) {
    case 'SET_ACTIVE_MENU':
      return {
        ...state,
        activeMenu: action.payload
      };
    case 'SET_IS_GROUPS_MODAL_VISIBLE':
      return {
        ...state,
        isGroupsModalVisible: action.payload
      };
    case 'TOGGLE_GROUPS_MODAL':
      return {
        ...state,
        isGroupsModalVisible: !state.isGroupsModalVisible
      };
    case 'SET_IS_DRAWER_VISIBLE':
      return {
        ...state,
        isDrawerVisible: action.payload
      };
    default:
      return state;
  }
}

const MenuContext = createContext<MenuState | null>(null);
const MenuDispatchContext = createContext<Dispatch<MenuAction> | null>(null);

function MenuProvider({ children }: PropsWithChildren) {
  const [state, dispatch] = useReducer(menuReducer, initialState);

  return (
    <MenuContext.Provider value={state}>
      <MenuDispatchContext.Provider value={dispatch}>{children}</MenuDispatchContext.Provider>
    </MenuContext.Provider>
  );
}

export { MenuContext, MenuDispatchContext, MenuProvider };
