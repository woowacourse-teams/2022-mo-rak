import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import { AxiosError } from 'axios';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import Box from '../../@common/Box/Box';
import Logo from '../../../assets/logo.svg';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import InvitationButtonGroup from '../InvitationButtonGroup/InvitationButtonGroup';
import { getIsJoinedGroup } from '../../../api/group';
import { GroupInterface } from '../../../types/group';
import { saveSessionStorageItem } from '../../../utils/storage';

function InvitationContainer() {
  const navigate = useNavigate();
  const location = useLocation();
  const [name, setName] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [isJoined, setIsJoined] = useState(false);
  const [groupCode, setGroupCode] = useState<GroupInterface['code']>('');
  const { invitationCode } = useParams() as { invitationCode: string };

  useEffect(() => {
    // TODO: 인자로 넘겨여할까? 너무 외부에 있는 변수에 의존하고 있다. 인자로 받아서 사용해주는 것이 올바른 코드가 아닐까? ex) invitationCode
    const fetchGetIsJoinedGroup = async () => {
      try {
        const res = await getIsJoinedGroup(invitationCode);
        const { groupCode, name, isJoined } = res.data;

        setGroupCode(groupCode);
        setName(name);
        setIsJoined(isJoined);
        setIsLoading(false);
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;
          if (errCode === '0201') {
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
      alert('이미 속해있는 그룹의 초대장입니다~');
    }
  }, [isJoined]);

  if (isLoading) return <div>로딩중</div>;

  return (
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
        <InvitationButtonGroup
          // NOTE: useParams으로 code들을 다 내려주고 있는데, navigate만 내려주지 않고 자식 컴포넌트에서
          // 다시 호출해서 navigate를 만드는 게 맞을까?
          navigate={navigate}
          invitationCode={invitationCode}
          groupCode={groupCode}
        />
      </FlexContainer>
    </Box>
  );
}

const StyledLogo = styled.img`
  width: 32rem;
`;

const StyledTitle = styled.p`
  font-size: 3.2rem;
`;

export default InvitationContainer;
