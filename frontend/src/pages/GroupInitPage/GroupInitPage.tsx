import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import GroupInitContainer from './components/GroupInitContainer/GroupInitContainer';
import { getDefaultGroup } from '../../api/group';
import { getLocalStorageItem } from '../../utils/storage';
import { StyledContainer } from './GroupInitPage.styles';
import { AxiosError } from 'axios';


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
      <GroupInitContainer />
    </StyledContainer>
  );
}

export default GroupInitPage;
