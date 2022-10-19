import { createContext, PropsWithChildren, useReducer, Dispatch } from 'react';
import { Member } from '../types/group';

type AuthState = {
  id: Member['id'] | null;
  name: Member['name'] | null;
};

type SetAuthAction = {
  type: 'SET_AUTH';
  payload: Pick<Member, 'id' | 'name'>;
};

type SetNameAction = {
  type: 'SET_NAME';
  payload: Member['name'];
};

type AuthAction = SetAuthAction | SetNameAction;

function authReducer(state: AuthState, action: AuthAction): AuthState {
  switch (action.type) {
    case 'SET_AUTH':
      return {
        ...state,
        id: action.payload.id,
        name: action.payload.name
      };
    case 'SET_NAME':
      return {
        ...state,
        name: action.payload
      };
    default:
      return state;
  }
}

const AuthContext = createContext<AuthState | null>(null);
const AuthDispatchContext = createContext<Dispatch<AuthAction> | null>(null);

const initialState = {
  id: null,
  name: null
} as const;

function AuthProvider({ children }: PropsWithChildren) {
  const [state, dispatch] = useReducer(authReducer, initialState);

  return (
    <AuthContext.Provider value={state}>
      <AuthDispatchContext.Provider value={dispatch}>{children}</AuthDispatchContext.Provider>
    </AuthContext.Provider>
  );
}

export { AuthContext, AuthDispatchContext, AuthProvider };
