import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import Box from '../../components/common/Box/Box';
import Logo from '../../assets/logo.svg';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';
import Button from '../../components/common/Button/Button';
import theme from '../../styles/theme';
import { getIsJoinedGroup, participateGroup } from '../../api/group';
import { GroupInterface } from '../../types/group';
import { saveSessionStorageItem } from '../../utils/storage';

// TODO: 페이지 추상화
function InvitationPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [name, setName] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [groupCode, setGroupCode] = useState<GroupInterface['code']>('');
  const [isJoined, setIsJoined] = useState(false);
  const { invitationCode } = useParams() as { invitationCode: string };

  // TODO: 다른 유저 로그인 후 테스트 필요
  const handleParticipateGroup = async () => {
    try {
      await participateGroup(invitationCode);
      navigate(`/groups/${groupCode}`);
    } catch (err) {
      if (err instanceof Error) {
        console.log(JSON.stringify(err));
      }
    }
  };

  const handleCancelInvitation = () => {
    if (window.confirm('거절하시겠습니까?')) {
      console.log('취소');
      navigate('/');
    }
  };

  useEffect(() => {
    // TODO: 인자로 넘겨여할까?
    const fetchGetIsJoinedGroup = async () => {
      try {
        const { groupCode, name, isJoined } = await getIsJoinedGroup(invitationCode);
        setGroupCode(groupCode);
        setName(name);
        setIsJoined(isJoined);
        setIsLoading(false);
      } catch (err) {
        if (err instanceof Error) {
          const statusCode = err.message;

          if (statusCode === '401') {
            // navigate('/', { state: { redirectUrl: location.pathname } });
            saveSessionStorageItem('redirectUrl', location.pathname);
            navigate('/');
            alert('로그인이 필요한 서비스입니다!');
          }
        }

        setIsLoading(true);
      }
    };

    fetchGetIsJoinedGroup();
  }, []);

  useEffect(() => {
    if (isJoined) {
      navigate(`/groups/${groupCode}`);
      // TODO: rendering blocking하는 것을 개선해주기
      alert('이미 속해있는 그룹의 초대장입니다~');
    }
  }, [isJoined]);

  if (isLoading) return <div>로딩중</div>;

  return (
    <StyledContainer>
      <Box width="60rem" minHeight="65.2rem" padding="9.2rem 0">
        <FlexContainer
          justifyContent="center"
          flexDirection="column"
          alignItems="center"
          gap="6.6rem"
        >
          <StyledLogo src={Logo} alt="logo" />
          <StyledTitle>
            {name}
            그룹으로 초대합니다
          </StyledTitle>
          <FlexContainer gap="1.2rem">
            <Button
              variant="filled"
              colorScheme={theme.colors.PURPLE_100}
              width="18.4rem"
              fontSize="2.4rem"
              padding="2rem 0"
              onClick={handleParticipateGroup}
            >
              수락
            </Button>
            <Button
              variant="filled"
              colorScheme={theme.colors.GRAY_400}
              width="18.4rem"
              fontSize="2.4rem"
              padding="2rem 0"
              onClick={handleCancelInvitation}
            >
              거절
            </Button>
          </FlexContainer>
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
  width: 32rem;
`;

const StyledTitle = styled.p`
  font-size: 3.2rem;
`;

export default InvitationPage;
