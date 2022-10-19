import { createContext, useReducer, PropsWithChildren, Dispatch } from 'react';
import { Menu } from '../types/menu';
type MenuState = {
  activeMenu: Menu;
  isVisibleGroupsModal: boolean;
};

type SetActiveMenuAction = {
  type: 'SET_ACTIVE_MENU';
  payload: Menu;
};

type SetIsVisibleGroupsModalAction = {
  type: 'SET_IS_VISIBLE_GROUPS_MODAL';
  payload: boolean;
};

type ToggleGroupsModalAction = {
  type: 'TOGGLE_GROUPS_MODAL';
};

type MenuAction = SetActiveMenuAction | SetIsVisibleGroupsModalAction | ToggleGroupsModalAction;

const initialState = {
  activeMenu: 'poll',
  // TODO: isGroupsModalVisible이 맞지 않을까?? 모달이 보이냐 vs 보이는 모달이냐의 차이
  isVisibleGroupsModal: false
} as const;

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
