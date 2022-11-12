import { AxiosError } from 'axios';
import { useEffect } from 'react';
import { Outlet, useNavigate, useParams } from 'react-router-dom';

import useAuthContext from '@/hooks/useAuthContext';
import useGroupMembersDispatchContext from '@/hooks/useGroupMembersDispatchContext';
import useNavigationBarContext from '@/hooks/useNavigationBarContext';

import { getGroupMembers } from '@/api/group';
import { Group } from '@/types/group';

function GroupRoute() {
  const navigate = useNavigate();
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const menuState = useNavigationBarContext();
  const authState = useAuthContext();
  const groupMembersDispatch = useGroupMembersDispatchContext();
  const activeMenu = menuState?.activeMenu;

  useEffect(() => {
    (async () => {
      try {
        const res = await getGroupMembers(groupCode);
        groupMembersDispatch({ type: 'SET_GROUP_MEMBERS', payload: res.data });
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;

          if (errCode === '1300') {
            alert('잘못된 접근입니다.');
            navigate('/');
          }
        }
      }
    })();
  }, [activeMenu, authState, groupCode]);

  return <Outlet />;
}

export default GroupRoute;
