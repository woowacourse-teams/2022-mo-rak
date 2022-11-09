import { useContext } from 'react';
import { GroupMembersContext } from '@/context/GroupMembersProvider';

function useGroupMembersContext() {
  const context = useContext(GroupMembersContext);

  if (!context) {
    throw new Error('GroupMembersContext를 찾을 수 없습니다.');
  }

  return context;
}

export default useGroupMembersContext;
