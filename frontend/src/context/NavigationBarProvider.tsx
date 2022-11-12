import { Dispatch, PropsWithChildren, createContext, useReducer } from 'react';

import { Menu } from '@/types/menu';

type NavigationBarState = {
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

type NavigationBarAction =
  | SetActiveMenuAction
  | SetIsGroupsModalVisibleAction
  | ToggleGroupsModalAction
  | SetIsDrawerVisibleAction;

const initialState = {
  activeMenu: 'poll',
  isDrawerVisible: false,
  isGroupsModalVisible: false
} as const;

function navigationBarReducer(
  state: NavigationBarState,
  action: NavigationBarAction
): NavigationBarState {
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

const NavigationBarContext = createContext<NavigationBarState | null>(null);
const NavigationBarDispatchContext = createContext<Dispatch<NavigationBarAction> | null>(null);

function NavigationBarProvider({ children }: PropsWithChildren) {
  const [state, dispatch] = useReducer(navigationBarReducer, initialState);

  return (
    <NavigationBarContext.Provider value={state}>
      <NavigationBarDispatchContext.Provider value={dispatch}>
        {children}
      </NavigationBarDispatchContext.Provider>
    </NavigationBarContext.Provider>
  );
}

export { NavigationBarContext, NavigationBarDispatchContext, NavigationBarProvider };
