import { useContext } from 'react';
import { GroupMembersContext } from '@/context/GroupMembersProvider';
import { CONTEXT_ERROR } from '@/constants/errorMessage';

function useGroupMembersContext() {
  const context = useContext(GroupMembersContext);

  if (!context) {
    throw new Error(CONTEXT_ERROR.NO_GROUP_MEMBERS_CONTEXT);
  }

  return context;
}

export default useGroupMembersContext;
