import { useContext, useEffect } from 'react';
import { Outlet, useParams } from 'react-router-dom';
import { getGroupMembers } from '../api/group';
import { MenuContext } from '../context/MenuProvider';
import useGroupMembersDispatchContext from '../hooks/useGroupMembersDispatchContext';
import { GroupInterface } from '../types/group';

function GroupRoute() {
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const menuState = useContext(MenuContext);
  const groupMembersDispatch = useGroupMembersDispatchContext();
  const activeMenu = menuState?.activeMenu;

  useEffect(() => {
    (async () => {
      try {
        const res = await getGroupMembers(groupCode);
        groupMembersDispatch({ type: 'SET_GROUP_MEMBERS', payload: res.data });
      } catch (err) {}
    })();
  }, [activeMenu]);

  return <Outlet />;
}

export default GroupRoute;
