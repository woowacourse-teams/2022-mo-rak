import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { getDefaultGroup } from '@/apis/group';
import { getLocalStorageItem } from '@/utils/storage';
import { AxiosError } from 'axios';
import { StyledContainer } from '@/pages/GroupInitPage/GroupInitPage.styles';
import GroupInitTitle from '@/pages/GroupInitPage/components/GroupInitTitle/GroupInitTitle';
import GroupInitForms from '@/pages/GroupInitPage/components/GroupInitForms/GroupInitForms';

function GroupInitPage() {
  const navigate = useNavigate();

  useEffect(() => {
    // TODO: 중복 로직해결
    // TODO: 다시 한 번 token을 가져오는 중복 로직해결
    const token = getLocalStorageItem<string>('token');
    if (token) {
      (async () => {
        try {
          const res = await getDefaultGroup();
          const { code: groupCode } = res.data;

          navigate(`/groups/${groupCode}`);
        } catch (err) {
          if (err instanceof AxiosError) {
            const statusCode = err.response?.status;

            if (statusCode === 404) {
              navigate('/init');
            }
          }
        }
      })();
    }
  }, []);

  return (
    <StyledContainer>
      <GroupInitTitle />
      <GroupInitForms />
    </StyledContainer>
  );
}

export default GroupInitPage;
