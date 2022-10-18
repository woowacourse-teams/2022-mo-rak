import { createContext, useReducer, PropsWithChildren, Dispatch } from 'react';
import { Member } from '../types/group';

type GroupMembersState = {
  groupMembers: Array<Member>;
};

type SetActiveMenuAction = {
  type: 'SET_GROUP_MEMBERS';
  payload: Array<Member>;
};

type GroupMembersAction = SetActiveMenuAction;

const GroupMembersContext = createContext<GroupMembersState | null>(null);
const GroupMembersDispatchContext = createContext<Dispatch<GroupMembersAction> | null>(null);

function groupMembersReducer(
  state: GroupMembersState,
  action: GroupMembersAction
): GroupMembersState {
  switch (action.type) {
    case 'SET_GROUP_MEMBERS':
      return {
        ...state,
        groupMembers: action.payload
      };
    default:
      return state;
  }
}

// TODO: groupCode도 넣기
const initialState = { groupMembers: [] };
function GroupMembersProvider({ children }: PropsWithChildren) {
  const [state, dispatch] = useReducer(groupMembersReducer, initialState);

  return (
    <GroupMembersContext.Provider value={state}>
      <GroupMembersDispatchContext.Provider value={dispatch}>
        {children}
      </GroupMembersDispatchContext.Provider>
    </GroupMembersContext.Provider>
  );
}

export { GroupMembersContext, GroupMembersDispatchContext, GroupMembersProvider };
