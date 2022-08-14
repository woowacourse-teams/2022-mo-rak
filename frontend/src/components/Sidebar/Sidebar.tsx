import React, { useState, useEffect, MouseEventHandler } from 'react';
import styled from '@emotion/styled';
import { Link, useNavigate } from 'react-router-dom';
import Logo from '../../assets/logo.svg';
import Poll from '../../assets/person-check.svg';
import Appointment from '../../assets/calendar-clock.svg';
import Plus from '../../assets/plus.svg';
import { createInvitationCode, getDefaultGroup, getGroups } from '../../api/group';
import { writeClipboard } from '../../utils/clipboard';
import { GroupInterface } from '../../types/group';
import Divider from '../@common/Divider/Divider';
import Setting from '../../assets/setting.svg';
import Menu from '../../assets/menu.svg';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import SidebarMembersProfile from '../SidebarMembersProfile/SidebarMembersProfile';
import { getLocalStorageItem, removeLocalStorageItem } from '../../utils/storage';

interface Props {
  groupCode: GroupInterface['code'];
  handleSetClickedMenu: (menu: string) => MouseEventHandler<HTMLDivElement>;
  clickedMenu: string;
}

function Sidebar({ groupCode, handleSetClickedMenu, clickedMenu }: Props) {
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);
  const [isClickedGroupList, setIsClickedGroupList] = useState(false);

  const [defaultGroup, setDefaultGroup] = useState<GroupInterface>();
  console.log(defaultGroup);

  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
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

  const handleShowGroupList = () => {
    setIsClickedGroupList(!isClickedGroupList);
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

  useEffect(() => {
    const fetchGetDefaultGroup = async () => {
      try {
        const res = await getDefaultGroup();
        setDefaultGroup(res.data);
      } catch (err) {
        if (err instanceof Error) {
          const statusCode = err.message;
          if (statusCode === '401') {
            removeLocalStorageItem('token');
            navigate('/');

            return;
          }

          if (statusCode === '404') {
            navigate('/init');
            console.log('속해있는 그룹이 없습니다.');
          }
        }
      }
    };
    const token = getLocalStorageItem('token');

    if (token) {
      fetchGetDefaultGroup();
    }
  }, []);

  if (isLoading) return <div>로딩중</div>;

  return (
    <StyledContainer>
      <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
      {/* 그룹 */}
      <StyledGroupContainer>
        <StyledMenuHeader>그룹</StyledMenuHeader>

        {/* default group */}
        <FlexContainer justifyContent="space-between">
          <FlexContainer gap="2rem">
            <StyledGroupImage src="https://us.123rf.com/450wm/zoomzoom/zoomzoom1803/zoomzoom180300055/97726350-%EB%B9%9B-%EA%B5%AC%EB%A6%84%EA%B3%BC-%ED%91%B8%EB%A5%B8-%EB%B4%84-%ED%95%98%EB%8A%98.jpg?ver=6" />
            <FlexContainer flexDirection="column">
              <StyledGroupTitle>{defaultGroup && defaultGroup.name}</StyledGroupTitle>
              <StyledGroupMemberCount>?명의 멤버</StyledGroupMemberCount>
            </FlexContainer>
          </FlexContainer>

          <StyledGroupIconGroup>
            {/* TODO: setting 아이콘은, host만 보이도록 해줘야한다. */}
            <StyledSettingIcon src={Setting} />
            <StyledGroupListIcon src={Menu} onClick={handleShowGroupList} />
          </StyledGroupIconGroup>
        </FlexContainer>

        {/* group list */}
        <StyledGroupListBox isClickedShowGroupList={isClickedGroupList}>
          <StyledGroupListContainer>
            {groups.map((group) => (
              <StyledGroupList to={`groups/${group.code}`} isDefaultGroup={groupCode === group.code}>
                <StyledGroupListImage src="https://us.123rf.com/450wm/zoomzoom/zoomzoom1803/zoomzoom180300055/97726350-%EB%B9%9B-%EA%B5%AC%EB%A6%84%EA%B3%BC-%ED%91%B8%EB%A5%B8-%EB%B4%84-%ED%95%98%EB%8A%98.jpg?ver=6" />
                <FlexContainer flexDirection="column">
                  <StyledGroupTitle>{group.name}</StyledGroupTitle>
                  <StyledGroupMemberCount>?명의 멤버</StyledGroupMemberCount>
                </FlexContainer>
              </StyledGroupList>
            ))}
          </StyledGroupListContainer>
          <StyledParticipateNewGroup>
            <img src={Plus} alt="participate-new-group-button" />
            <StyledGroupText>새로운 그룹 참가</StyledGroupText>
          </StyledParticipateNewGroup>
          <StyledCreateNewGroup>
            <img src={Plus} alt="create-new-group-button" />
            <StyledInviteText>새로운 그룹 생성</StyledInviteText>
          </StyledCreateNewGroup>
        </StyledGroupListBox>
      </StyledGroupContainer>

      {/* 기능 */}
      <Divider />
      <StyledFeatureContainer>
        <StyledMenuHeader>기능</StyledMenuHeader>
        <FlexContainer flexDirection="column">
          <StyledPollMenu onClick={handleSetClickedMenu('poll')} isClicked={clickedMenu === 'poll'}>
            <StyledPollIcon src={Poll} />
            <StyledFeatureTitle>투표하기</StyledFeatureTitle>
          </StyledPollMenu>
          <StyledAppointmentMenu onClick={handleSetClickedMenu('appointment')} isClicked={clickedMenu === 'appointment'}>
            <StyledAppointmentIcon src={Appointment} />
            <StyledFeatureTitle>약속잡기</StyledFeatureTitle>
          </StyledAppointmentMenu>
        </FlexContainer>
      </StyledFeatureContainer>

      {/* 멤버 목록 */}
      <Divider />
      <StyledMemberListContainer>
        <SidebarMembersProfile groupCode={groupCode} />
      </StyledMemberListContainer>

      {/* 초대링크 */}
      <StyledInvitationLink onClick={handleCopyInviationCode}>
        <img src={Plus} alt="inivation-link" />
        <StyledInviteText>새로운 멤버 초대</StyledInviteText>
      </StyledInvitationLink>
    </StyledContainer>
  );
}

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

// 그룹
const StyledGroupListBox = styled.div<{isClickedShowGroupList: boolean}>(({ theme, isClickedShowGroupList }) => `
  visibility: 'hidden';
  opacity: 0;
  transition: visibility 0s, opacity 0.2s ease-in-out;
  width: 24rem;
  background: ${theme.colors.WHITE_100};
  position: absolute;
  right: -252px;
  top: 44px;
  border-radius: 12px;
  max-height: 45.2rem;

  ${isClickedShowGroupList && `
  visibility: visible;
  opacity: 1;
  `}
`);

const StyledGroupListContainer = styled.div`
  overflow-y: auto;
  max-height: 32.4rem;
`;

const StyledSettingIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;

  &:hover {
    transform: rotate(0.5turn);
    transition: all 0.3s linear;
  }
`;

const StyledGroupListIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;
  &:hover {
    transform: scale(1.1, 1.1);
    transition: all 0.3s linear;
  }
`;

const StyledGroupImage = styled.img`
  width: 8rem;
  height: 8rem;
  border-radius: 1.2rem;
`;

const StyledGroupListImage = styled.img`
  width: 5.2rem;
  height: 5.2rem;
  border-radius: 0.8rem;
`;

const StyledGroupTitle = styled.div`
  font-size: 1.6rem;
  margin-bottom: 1.2rem;
`;

const StyledGroupMemberCount = styled.div(({ theme }) => `
  font-size: 1.6rem;
  color: ${theme.colors.GRAY_400};
`);

const StyledGroupContainer = styled.div`
position: relative;
  width: 100%;
  margin-bottom: 2.8rem;
`;

const StyledMenuHeader = styled.div`
width: 100%;
font-size: 2rem;
text-align: left;
margin-bottom: 2rem;
`;

const StyledGroupList = styled(Link)<{ isDefaultGroup: boolean }>(
  ({ theme, isDefaultGroup }) => `
  display: flex;
  gap: 2rem;
  padding: 2rem;
  text-decoration: none;
  margin: 1.2rem;
  color: ${theme.colors.BLACK_100};

  ${isDefaultGroup
    && `
    background: ${theme.colors.GRAY_100}; 
    border-radius: 10px;
    `
}
`
);

const StyledGroupIconGroup = styled.div`
  display: flex;
  align-items: flex-start;
  gap: 0.4rem;
  padding-right: 4rem;
`;

const StyledParticipateNewGroup = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

const StyledCreateNewGroup = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
  width: 100%;
  padding: 2rem;

  &:hover {
    transform: scale(1.05);
    transition: all 0.2s linear;
  }
`;

const StyledGroupText = styled.p`
  font-size: 2rem;
`;

// 기능
const StyledFeatureContainer = styled.div`
  margin: 2.8rem 0;
`;

const StyledPollIcon = styled.img`
  width: 2.4rem;
`;

const StyledAppointmentIcon = styled.img`
  width: 2.4rem;
`;

const StyledFeatureTitle = styled.div`
  font-size: 1.6rem;
`;

const StyledPollMenu = styled.div<{
  isClicked: boolean
}>(
  ({ isClicked, theme }) => `
  display: flex;
  gap: 2rem;
  cursor: pointer;
  padding: 2rem;
  ${isClicked && `
    background: ${theme.colors.GRAY_100}; 
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;`};

  &:hover {
    background: ${!isClicked && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
`
);

const StyledAppointmentMenu = styled.div<{
  isClicked: boolean
}>(
  ({ isClicked, theme }) => `
  display: flex;
  gap: 2rem;
  cursor: pointer;
  padding: 2rem;
  
  ${isClicked && `
  background: ${theme.colors.GRAY_100}; 
  border-top-left-radius: 4rem; 
  border-bottom-left-radius: 4rem;
  `};

  &:hover {
    background: ${!isClicked && theme.colors.TRANSPARENT_GRAY_100_80};
    border-top-left-radius: 4rem; 
    border-bottom-left-radius: 4rem;
  } 
  
`
);

// 멤버
const StyledMemberListContainer = styled.div`
  margin: 2.8rem 0;
`;

// 초대링크
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

const StyledInviteText = styled.p`
  font-size: 2rem;
`;

export default Sidebar;
