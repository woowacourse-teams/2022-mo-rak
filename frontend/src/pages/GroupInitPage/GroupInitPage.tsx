import React, { useEffect } from 'react';
import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';

import Logo from '../../assets/logo.svg';

import GroupInitContainer from '../../components/GroupInit/GroupInitContainer/GroupInitContainer';
import { getDefaultGroup } from '../../api/group';
import { getLocalStorageItem, removeLocalStorageItem } from '../../utils/storage';

function GroupInitPage() {
  const navigate = useNavigate();

  useEffect(() => {
    const fetchGetDefaultGroup = async () => {
      try {
        const { code: groupCode } = await getDefaultGroup();

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
      <StyledLogo src={Logo} alt="logo" />
      <GroupInitContainer />
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 6.4rem;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
`;

const StyledLogo = styled.img`
  width: 35.6rem;
`;

export default GroupInitPage;
