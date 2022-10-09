import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import GroupInitContainer from './components/GroupInitContainer/GroupInitContainer';
import { getDefaultGroup } from '../../api/group';
import { getLocalStorageItem, removeLocalStorageItem } from '../../utils/storage';
import { StyledContainer } from './GroupInitPage.styles';

function GroupInitPage() {
  const navigate = useNavigate();

  useEffect(() => {
    const fetchGetDefaultGroup = async () => {
      try {
        const res = await getDefaultGroup();
        const { code: groupCode } = res.data;

        navigate(`/groups/${groupCode}`);
      } catch (err) {
        if (err instanceof Error) {
          const statusCode = err.message;
          if (statusCode === '401') {
            removeLocalStorageItem('token');
            navigate('/');

            return;
          }
        }
        console.log(
          '그룹 생성 및 참가 페이지에서 로그인을 했지만, 속해있는 그룹이 없는 것을 확인했습니다.'
        );
      }
    };

    // TODO: 중복로직 해결 필요
    // TODO: 다시 한 번 token을 가져오는 로직 개선필요
    const token = getLocalStorageItem('token');

    if (token) {
      fetchGetDefaultGroup();
    }
  }, []);

  return (
    <StyledContainer>
      <GroupInitContainer />
    </StyledContainer>
  );
}

export default GroupInitPage;
