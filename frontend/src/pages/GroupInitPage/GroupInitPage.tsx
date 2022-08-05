import React, { useEffect } from 'react';
import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';

import Logo from '../../assets/logo.svg';

import GroupCreateForm from '../../components/GroupInit/GroupCreateForm/GroupCreateForm';
import GroupParticipateForm from '../../components/GroupInit/GroupParticipateForm/GroupParticipateForm';
import Box from '../../components/common/Box/Box';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';
import { getDefaultGroup } from '../../api/group';
import { getLocalStorageItem, removeLocalStorageItem } from '../../utils/storage';

function GroupInitPage() {
  const navigate = useNavigate();
  // TODO: 중복로직 해결 필요
  // TODO: 다시 한 번 token을 가져오는 로직 개선필요
  const token = getLocalStorageItem('token');
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

    if (token) {
      fetchGetDefaultGroup();
    }
  }, []);

  return (
    <StyledContainer>
      <StyledLogo src={Logo} alt="logo" />
      {/* TODO: 얘만 따로 컴포넌트로 빼주자 - GroupInitFormContainer?? */}
      {/* 만약, GroupCreateForm이랑 GroupParticipateForm가 Box로 감싸져있지 않았다면, 빼주지 않아도 될듯?  */}
      <Box width="60rem" minHeight="51.6rem" padding="8.4rem 3.2rem">
        <FlexContainer flexDirection="column" gap="6.8rem">
          <GroupCreateForm />
          <GroupParticipateForm />
        </FlexContainer>
      </Box>
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
