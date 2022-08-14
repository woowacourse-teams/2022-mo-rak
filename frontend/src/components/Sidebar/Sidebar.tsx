import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { Link, useParams, useNavigate } from 'react-router-dom';
import Logo from '../../assets/logo.svg';
import LinkIcon from '../../assets/link.svg';
import Close from '../../assets/close-button.svg';
import { createInvitationCode, getGroups } from '../../api/group';
import { writeClipboard } from '../../utils/clipboard';
import { GroupInterface } from '../../types/group';
import Slack from '../../assets/slack.svg';
import TextField from '../@common/TextField/TextField';
import Input from '../@common/Input/Input';
import Button from '../@common/Button/Button';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import theme from '../../styles/theme';

function Sidebar() {
  const { groupCode } = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);
  const [isClickedSlackMenu, setIsClickedSlackMenu] = useState(false);

  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleSetIsClickedSlackMenu = () => {
    setIsClickedSlackMenu(!isClickedSlackMenu);
  };

  const handleCopyInviationCode = async () => {
    try {
      if (groupCode) {
        const res = await createInvitationCode(groupCode);
        const invitationCode = res.headers.location.split('groups/in')[1];
        const invitationLink = `
        링크를 클릭하거나, 참가 코드를 입력해주세요😀
        url: ${process.env.CLIENT_URL}/invite/${invitationCode}}
        코드: ${invitationCode}
        `;

        writeClipboard(invitationLink);
        alert('초대링크가 클립보드에 복사되었습니다💌');
      }
    } catch (err) {
      alert(err);
    }
  };

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const res = await getGroups();
        setGroups(res.data);
        setIsLoading(false);
      } catch (err) {
        alert(err);
        setIsLoading(true);
      }
    };

    fetchGroups();
  }, []);

  if (isLoading) return <div>로딩중</div>;

  return (
    <>
      <StyledContainer>
        <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
        <StyledGroupContainer>
          <StyledGroupHeaderButton type="button">Groups</StyledGroupHeaderButton>
          <StyledContent>
            {groups.map((group) => (
              <StyledGroupButton
                to={`groups/${group.code}`}
                isDefaultGroup={groupCode === group.code}
              >
                {group.name}
              </StyledGroupButton>
            ))}
          </StyledContent>
        </StyledGroupContainer>
        <StyledInvitationLink onClick={handleCopyInviationCode}>
          <img src={LinkIcon} alt="inivation-link" />
          <p>초대 링크 복사</p>
        </StyledInvitationLink>

        {/* TODO: 슬랙 메뉴 임시 (사이드바 pr merge되면, 대체하기) */}
        <StyledSlackMenu onClick={handleSetIsClickedSlackMenu}>슬랙 메뉴</StyledSlackMenu>
      </StyledContainer>

      <StyledSlackModalContainer isClickedSlackMenu={isClickedSlackMenu}>
        <StyledSlackModal>
          <StyledTop>
            <StyledSlackLogo src={Slack} alt="slack-logo" />
            <StyledHeaderText>슬랙 채널과 연동해보세요!</StyledHeaderText>
            <StyledGuideText>슬랙 채널과 연동하면, 그룹의 새소식을 슬랙으로 받아볼 수 있어요.</StyledGuideText>
            <StyledCloseButton onClick={handleSetIsClickedSlackMenu} src={Close} alt="close-button" />
            <StyledTriangle />
          </StyledTop>
          <StyledBottom>
            <FlexContainer flexDirection="column" alignItems="center" gap="2.4rem">
              <TextField
                variant="filled"
                colorScheme={theme.colors.WHITE_100}
                borderRadius="10px"
                padding="1.6rem 10rem"
                width="50.4rem"
              >
                <Input placeholder="슬랙 채널 url 입력 후, 확인버튼을 누르면 연동 끝!" fontSize="1.6REM" required />
                <StyledLinkIcon src={LinkIcon} alt="link-icon" />
              </TextField>
              <Button colorScheme="#ECB22E" variant="filled" width="14rem" padding="1.6rem 4rem" fontSize="1.6rem">확인</Button>
            </FlexContainer>
          </StyledBottom>
        </StyledSlackModal>
      </StyledSlackModalContainer>
    </>
  );
}

// 팝업 스타일
const StyledSlackModalContainer = styled.div<{isClickedSlackMenu: boolean}>(({ theme, isClickedSlackMenu }) => `
  display: ${isClickedSlackMenu ? 'flex' : 'none'};
  background-color: ${theme.colors.TRANSPARENT_BLACK_100_25};
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100vw;
`);

const StyledSlackModal = styled.div(({ theme }) => `
  position: relative;
  background-color: ${theme.colors.WHITE_100};
  border-radius: 12px;
  width: 68rem;
  height: 41.6rem;
`);

const StyledSlackLogo = styled.img`
  width: 8rem;
  display: block;
  margin: 0 auto ;
  margin-bottom: 2rem;
`;

const StyledHeaderText = styled.div`
  font-size: 2rem;
  text-align: center;
  margin-bottom: 1.2rem;
`;

const StyledGuideText = styled.div`
  font-size: 1.6rem;
  text-align: center;
`;

const StyledTop = styled.div`
  position: relative;
  height: calc(100% / 2);
  padding-top: 2.4rem;
`;

const StyledCloseButton = styled.img`
  position: absolute;
  right: 2.4rem;
  top: 2.4rem;
  cursor: pointer;

  &:hover {
    transform: scale(1.1);
    transition: all 0.3s linear;
  }
`;

const StyledTriangle = styled.div`
  position: absolute;
  border-left: 2rem solid transparent;
  border-right: 2rem solid transparent;
  border-top: 2rem solid ${theme.colors.WHITE_100};
  width: 0;
  bottom: -2.8rem;
  right: 50%;
  transform: translate(50%,-50%);
`;

const StyledBottom = styled.div(({ theme }) => `
  background: ${theme.colors.YELLOW_50};
  height: calc(100% / 2);
  padding-top: 4.4rem;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
`);

const StyledLinkIcon = styled.img`
  position: absolute;
  left: 1.2rem;
  top: 1.2rem;
`;

// TODO: 슬랙 메뉴 임시 (사이드바 pr merge되면, 대체하기)
const StyledSlackMenu = styled.div`
  font-size: 1.6rem;
`;

const StyledContainer = styled.div(
  ({ theme }) => `
  z-index: 1; 
  width: 36.4rem;
  height: 100vh;
  position: sticky;
  top: 0;
  border-right: 0.1rem solid ${theme.colors.GRAY_200};
  background: ${theme.colors.WHITE_100};
  padding: 4rem;
  gap: 2rem;
`
);

const StyledLogo = styled.img`
  width: 12rem;
  cursor: pointer;
`;

const StyledInvitationLink = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  position: absolute;
  bottom: 3.6rem;
  left: 3.6rem;
  gap: 1.2rem;
  font-size: 1.6rem;
`;

const StyledGroupContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 2.8rem;
`;

const StyledContent = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1.6rem;
`;

const StyledGroupHeaderButton = styled.button`
  width: 100%;
  font-size: 1.6rem;
  text-align: left;
`;

const StyledGroupButton = styled(Link)<{ isDefaultGroup: boolean }>(
  ({ theme, isDefaultGroup }) => `
  width: 100%;
  font-size: 1.6rem;
  color: ${isDefaultGroup ? theme.colors.BLACK_100 : theme.colors.GRAY_400};
  text-align: left;
  
`
);

export default Sidebar;
