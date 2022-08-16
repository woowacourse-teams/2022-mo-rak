import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useNavigate, useParams } from 'react-router-dom';
import Logo from '../../assets/logo.svg';
import LinkIcon from '../../assets/link.svg';
import Close from '../../assets/close-button.svg';
import { getGroups } from '../../api/group';
import { GroupInterface } from '../../types/group';
import Slack from '../../assets/slack.svg';
import TextField from '../@common/TextField/TextField';
import Input from '../@common/Input/Input';
import Modal from '../@common/Modal/Modal';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import theme from '../../styles/theme';
import Plus from '../../assets/plus.svg';

import Divider from '../@common/Divider/Divider';
import SidebarGroupMenu from '../SidebarGroupMenu/SidebarGroupMenu';

import SidebarMembersProfileMenu from '../SidebarMembersProfileMenu/SidebarMembersProfileMenu';
import SidebarFeatureMenu from '../SidebarFeatureMenu/SidebarFeatureMenu';
import SidebarInvitationMenu from '../SidebarInvitationMenu/SidebarInvitationMenu';
import SidebarSlackMenu from '../SidebarSlackMenu/SidebarSlackMenu';

function Sidebar() {
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);
  const [activeGroupMenu, setActiveGroupMenu] = useState<null | string>(null);

  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };

  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleSetActiveGroupMenu = (menu: null | string) => () => {
    setActiveGroupMenu(menu);
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

        {/* 그룹 */}
        {/* TODO: handleSetActiveGroupMenu 넘겨주는 방식(하나로 넘겨줄 수는 없을까?) */}
        <SidebarGroupMenu
          onClickCreateMenu={handleSetActiveGroupMenu('create')}
          onClickParticipateMenu={handleSetActiveGroupMenu('participate')}
          groupCode={groupCode}
          groups={groups}
        />

        {/* 기능 */}
        <Divider />
        <SidebarFeatureMenu groupCode={groupCode} />

        {/* 멤버 목록 */}
        <Divider />
        <SidebarMembersProfileMenu groupCode={groupCode} />

        <StyledBottomMenu>
          {/* 슬랙연동 */}
          <SidebarSlackMenu onClickMenu={handleSetActiveGroupMenu('slack')} />

          {/* 초대링크 */}
          <SidebarInvitationMenu groupCode={groupCode} />
        </StyledBottomMenu>

      </StyledContainer>

      <Modal isVisible={activeGroupMenu === 'slack'} close={handleSetActiveGroupMenu(null)}>
        {/* 슬랙 메뉴 */}
        <StyledModalContainer>
          <StyledTop>
            <StyledSlackLogo src={Slack} alt="slack-logo" />
            <StyledHeaderText>슬랙 채널과 연동해보세요!</StyledHeaderText>
            <StyledGuideText>슬랙 채널과 연동하면, 그룹의 새소식을 슬랙으로 받아볼 수 있어요</StyledGuideText>
            <StyledCloseButton onClick={handleSetActiveGroupMenu(null)} src={Close} alt="close-button" />
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
              <StyledButton>확인</StyledButton>
            </FlexContainer>
          </StyledBottom>
        </StyledModalContainer>
      </Modal>

      {/* 그룹 생성 */}
      <Modal isVisible={activeGroupMenu === 'create'} close={handleSetActiveGroupMenu(null)}>
        <StyledModalContainer>
          <StyledTop>
            <StyledSmallLogo src={Logo} alt="logo" />
            <StyledHeaderText>그룹 생성</StyledHeaderText>
            <StyledGuideText>새로운 그룹을 빠르고 쉽게 생성해보세요</StyledGuideText>
            <StyledCloseButton onClick={handleSetActiveGroupMenu(null)} src={Close} alt="close-button" />
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
                <Input placeholder="그룹 이름을 입력하면 생성 완료!" fontSize="1.6REM" required />
                <StyledLinkIcon src={Plus} alt="plus-icon" />
              </TextField>
              <StyledButton>생성하기</StyledButton>
            </FlexContainer>
          </StyledBottom>
        </StyledModalContainer>
      </Modal>

      {/* 그룹 참가 */}
      <Modal isVisible={activeGroupMenu === 'participate'} close={handleSetActiveGroupMenu(null)}>
        <StyledModalContainer>
          <StyledTop>
            <StyledSmallLogo src={Logo} alt="logo" />
            <StyledHeaderText>그룹 참가</StyledHeaderText>
            <StyledGuideText>새로운 그룹에도 참가해보세요</StyledGuideText>
            <StyledCloseButton onClick={handleSetActiveGroupMenu(null)} src={Close} alt="close-button" />
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
                <Input placeholder="그룹 코드를 입력하면 참가 완료!" fontSize="1.6REM" required />
                <StyledLinkIcon src={Plus} alt="plus-icon" />
              </TextField>
              <StyledButton>참가하기</StyledButton>
            </FlexContainer>
          </StyledBottom>
        </StyledModalContainer>
      </Modal>
    </>
  );
}

const StyledModalContainer = styled.div(({ theme }) => `
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
  height: 50%;
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
  height: 50%;
  padding-top: 4.4rem;
  border-bottom-left-radius: 12px;
  border-bottom-right-radius: 12px;
`);

const StyledLinkIcon = styled.img`
  position: absolute;
  left: 1.2rem;
  top: 1.2rem;
`;

const StyledButton = styled.button`
  background-color: ${theme.colors.YELLOW_200};
  color: ${theme.colors.WHITE_100};
  width: 14rem; 
  padding: 1.6rem 4rem;
  font-size: 1.6rem;
  position: relative;
  text-align: center;
  border-radius: 15px;

  &:hover {
    transform: scale(1.1);
    transition: all 0.3s linear;
  }
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
  padding-left: 4rem;
  gap: 2rem;
  border: none;
`
);

const StyledLogo = styled.img`
  display: block;
  margin: 2rem auto;  
  width: 12rem;
  cursor: pointer;
  padding-right: 4rem;
`;

const StyledSmallLogo = styled.img`
  display: block;
  margin: 2rem auto;  
  width: 8.8rem;
  cursor: pointer;
`;

const StyledBottomMenu = styled.div`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  position: absolute;
  bottom: 4rem;
`;

export default Sidebar;
