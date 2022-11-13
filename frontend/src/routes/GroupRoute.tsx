import { AxiosError } from 'axios';
import { useEffect } from 'react';
import { Outlet, useNavigate, useParams } from 'react-router-dom';
import { getGroupMembers } from '@/api/group';
import useAuthContext from '@/hooks/useAuthContext';
import useGroupMembersDispatchContext from '@/hooks/useGroupMembersDispatchContext';
import useNavigationBarContext from '@/hooks/useNavigationBarContext';
import { Group } from '@/types/group';
import { GROUP_ERROR } from '@/constants/errorMessage';

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
            // TODO: 두 번 alert가 나오는 문제 존재
            alert(GROUP_ERROR.NOT_PARTICIPATED);
            navigate('/');
          }
        }
      }
    })();
  }, [activeMenu, authState, groupCode]);

  return <Outlet />;
}

export default GroupRoute;
