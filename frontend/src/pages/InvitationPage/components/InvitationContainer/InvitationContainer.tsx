import { useEffect, useState } from 'react';
import { StyledLogo, StyledTitle } from './InvitationContainer.styles';
import { AxiosError } from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import Box from '../../../../components/Box/Box';
import Logo from '../../../../assets/logo.svg';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import InvitationButtonGroup from '../InvitationButtonGroup/InvitationButtonGroup';
import { getIsJoinedGroup } from '../../../../api/group';
import { Group } from '../../../../types/group';

function InvitationContainer() {
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [groupCode, setGroupCode] = useState<Group['code']>('');
  const { invitationCode } = useParams() as { invitationCode: string };

  useEffect(() => {
    (async () => {
      try {
        const res = await getIsJoinedGroup(invitationCode);
        const { groupCode, name, isJoined } = res.data;

        if (isJoined) {
          navigate(`/groups/${groupCode}`);
          alert('이미 속해있는 그룹의 초대장입니다~');
        }

        setGroupCode(groupCode);
        setName(name);
        setIsLoading(false);
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;

          if (errCode === '1301') {
            alert('유효하지 않은 초대장입니다');
            navigate('/');
          }
        }

        setIsLoading(true);
      }
    })();
  }, []);

  return (
    <Box width="60rem" padding="9.2rem 0">
      <FlexContainer
        justifyContent="center"
        flexDirection="column"
        alignItems="center"
        gap="6.6rem"
      >
        <StyledLogo src={Logo} alt="logo" />
        {!isLoading ? (
          <>
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
          </>
        ) : (
          <div>로딩중</div>
        )}
      </FlexContainer>
    </Box>
  );
}

export default InvitationContainer;
