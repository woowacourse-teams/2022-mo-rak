import { useContext } from 'react';
import { GroupMembersDispatchContext } from '@/context/GroupMembersProvider';
import { CONTEXT_ERROR } from '@/constants/errorMessage';

function useGroupMembersDispatchContext() {
  const context = useContext(GroupMembersDispatchContext);

  if (!context) {
    throw new Error(CONTEXT_ERROR.NO_GROUP_MEMBERS_DISPATCH_CONTEXT);
  }

  return context;
}

export default useGroupMembersDispatchContext;
