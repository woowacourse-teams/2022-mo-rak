import { useEffect } from 'react';
import { Outlet, useParams } from 'react-router-dom';
import { getGroupMembers } from '../api/group';
import useAuthContext from '../hooks/useAuthContext';
import useGroupMembersDispatchContext from '../hooks/useGroupMembersDispatchContext';
import useMenuContext from '../hooks/useMenuContext';
import { Group } from '../types/group';

function GroupRoute() {
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const menuState = useMenuContext();
  const authState = useAuthContext();
  const groupMembersDispatch = useGroupMembersDispatchContext();
  const activeMenu = menuState?.activeMenu;

  useEffect(() => {
    (async () => {
      try {
        const res = await getGroupMembers(groupCode);
        groupMembersDispatch({ type: 'SET_GROUP_MEMBERS', payload: res.data });
      } catch (err) {}
    })();
  }, [activeMenu, authState]);

  return <Outlet />;
}

export default GroupRoute;
