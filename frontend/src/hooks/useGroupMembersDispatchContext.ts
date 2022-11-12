import { useContext } from 'react';

import { GroupMembersDispatchContext } from '@/context/GroupMembersProvider';

function useGroupMembersDispatchContext() {
  const context = useContext(GroupMembersDispatchContext);

  if (!context) {
    throw new Error('GroupMembersProvider를 찾을 수 없습니다.');
  }

  return context;
}

export default useGroupMembersDispatchContext;
