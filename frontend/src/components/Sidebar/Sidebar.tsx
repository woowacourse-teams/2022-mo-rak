import React, { useState, useEffect, MouseEventHandler } from 'react';
import styled from '@emotion/styled';
import { Link, useNavigate } from 'react-router-dom';
import Logo from '../../assets/logo.svg';
import Poll from '../../assets/person-check.svg';
import Appointment from '../../assets/calendar-clock.svg';
import Plus from '../../assets/plus.svg';
import { createInvitationCode, getGroups } from '../../api/group';
import { writeClipboard } from '../../utils/clipboard';
import { GroupInterface } from '../../types/group';
import Divider from '../@common/Divider/Divider';
import Setting from '../../assets/setting.svg';
import Menu from '../../assets/menu.svg';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import Avatar from '../@common/Avatar/Avatar';

interface Props {
  groupCode: GroupInterface['code'];
  handleSetClickedMenu: (menu: string) => MouseEventHandler<HTMLDivElement>;
  clickedMenu: string;
}

function Sidebar({ groupCode, handleSetClickedMenu, clickedMenu }: Props) {
  const [isLoading, setIsLoading] = useState(true);
  const [groups, setGroups] = useState<Array<GroupInterface>>([]);

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
    <StyledContainer>
      <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
      {/* 그룹 */}
      <StyledGroupContainer>
        <StyledMenuHeader>그룹</StyledMenuHeader>
        <FlexContainer justifyContent="space-between">
          <FlexContainer gap="2rem">
            <StyledGroupImage />
            <FlexContainer flexDirection="column">
              <StyledGroupTitle>모락</StyledGroupTitle>
              <StyledGroupMemberCount>6명의 멤버</StyledGroupMemberCount>
            </FlexContainer>
          </FlexContainer>

          <StyledGroupIconGroup>
            <StyledSettingIcon src={Setting} />
            <StyledGroupListIcon src={Menu} />
          </StyledGroupIconGroup>
        </FlexContainer>

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
        <StyledMenuHeader>멤버 목록 (멤버수)</StyledMenuHeader>

        <StyledMembersProfileContainer>
          <FlexContainer alignItems="center" gap="2rem">
            <Avatar profileUrl="http://image.auction.co.kr/itemimage/20/f2/a3/20f2a30b36.jpg" name="" />
            <StyledName>어피치(나)</StyledName>
          </FlexContainer>

          <FlexContainer alignItems="center" gap="2rem">
            <Avatar profileUrl="https://item.kakaocdn.net/do/c5c470298d527ef65eb52883f0f186c49f5287469802eca457586a25a096fd31" name="" />
            <StyledName>춘식이(호스트)</StyledName>
          </FlexContainer>

          <FlexContainer alignItems="center" gap="2rem">
            <Avatar profileUrl="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxIHBg8SBw4PEhATDg0PFRAPEA8ODQ0RFREWFhURExYYKCggGBslHRUfITEhJSkrLi4uFx8zODMtNyg5OisBCgoKDg0OFw8QGjIlHSItNy0tKy4tKzctLy0tKzgtLS0tLSstLi0rNy0tLC0tKy0rOC0tKy03LS0rLTctKy0rN//AABEIAOAA4QMBIgACEQEDEQH/xAAaAAEAAgMBAAAAAAAAAAAAAAAABAUCAwYB/8QANhABAAECAgYIBAUFAQAAAAAAAAECAwQRBSExUWFxEhMiMkGRocEzcoGxNFJiotEUQoLh8SP/xAAZAQEBAQEBAQAAAAAAAAAAAAAAAwIBBAX/xAAdEQEBAQEAAgMBAAAAAAAAAAAAAQIRAzESIUET/9oADAMBAAIRAxEAPwDrAH0XzQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAe00zVPZiZ5a26nB3KtlE/XKHOu8aBInBXI/s8piWmuiaJ7cTHOMjsOViA64AAAAAAAAAAAAAAAAAAAAAAJ+F0f0ozv6v0+P1Z6OwuURXcjX4Ru4rBLW/yK5x+1jbtxbjKiIjkyBNQeVUxVGVURMcXoCBidHRMZ2NU/l8J5KyqOjOVW10SHj8L1tHSojtR+6FM7/KnrH7FSAqkAAAAAAAAAAAAAAAAAANuEtddfiPDbPKGpYaIo7VU8oZ1eRrM7VlGqNQCC4AAAAACm0hZ6rETlsnXHujLTS1Gdqmd1WXnCrXzexDU5QBpkAAAAAAAAAAAAAAAAWeiPhVfNH2VifomvK5VG+Iny/wCs79N49rMBBYAAAAABE0p+F/ypVCz0tX2KaeOfkrFsekd+wBtgAAAAAAAAAAAAAAAAZ2LnU3YqjwnzhgDroaKorpiadkxm9VOAxfUz0bnd3/l/0tonONSGpxfN7ABl0AAJnKNYrdIYzOJotTzn2h2TrlvEXGXuvvzMbNkcmkF4hQB1wAAAAAAAAAAAAAAAAAASMNjKrGqNdO6fZHHLOuy8XNrHUXNs5Tun+UiKoq2TDniJy2MXxtzyOimctrRdxdFrbVE8I1ypJnPaH8y+RLxOOqvRlR2afWUQG5OMW9AHXAAAAAAAAAAAAAAAAAAAZ2bNV6rK3H8Qs8Po+m3rudqf2s3UjUzarLdmq7P/AJ0zP2Srejaqu/MR6ytYjKNQnd1SYiDToymO9VVPLKGcaOo/V5pY58q18YiTo6jj5sKtGUz3aqo55SnB8qfGKq5oyqO5MT6Si3bNVr4lMx9l+TGca3Zus3Ec6Le/o+m58Pszw2eSsv2KrFWVyPr4SpNSp3NjWA0yAAAAAAAAAAAAAAJGDwk4ic51U79/CHmDw/8AUXOEbZ9l1RTFFMRTGUQxrXPpTOe/by3bi1RlbjKGQIqgAAAAAAADG5RFynKuM4ZAKfGYObE5066fWOaK6GqOlTlVsU2Nw39PX2e7OzhwVzrv1UtZ59xHAUTAAAAAAAAAACmOlVEU7Z1Cbou10rs1TsjZzly3kdk7eLDDWYsWoiPrO+W0HnegAAAAAAAAAAAAYX7UXrUxV4+k72YDnrlE265irbE5PFhpWzlMVRyn2V70S9iFnKAOsgAAAAAAAC60fb6GFp46/NS7XQ0R0aIiPCIhPyVTxx6AkqAAAAAAAAAAAAAA1Yu31uHqjhn9YULo3P3aehdqjdVMeqvjqfkjEBRIAAAAAAABlajO7T80fd0Cgs/Gp+an7r9LyK+MATUAAAAAAAAAAAAAAFHjIyxVfzLxR438XXz9lPH7Y8nppAVRAAAAf//Z" name="" />
            <StyledName>이름없음</StyledName>
          </FlexContainer>

          <FlexContainer alignItems="center" gap="2rem">
            <Avatar profileUrl="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxIHBg8SBw4PEhATDg0PFRAPEA8ODQ0RFREWFhURExYYKCggGBslHRUfITEhJSkrLi4uFx8zODMtNyg5OisBCgoKDg0OFw8QGjIlHSItNy0tKy4tKzctLy0tKzgtLS0tLSstLi0rNy0tLC0tKy0rOC0tKy03LS0rLTctKy0rN//AABEIAOAA4QMBIgACEQEDEQH/xAAaAAEAAgMBAAAAAAAAAAAAAAAABAUCAwYB/8QANhABAAECAgYIBAUFAQAAAAAAAAECAwQRBSExUWFxEhMiMkGRocEzcoGxNFJiotEUQoLh8SP/xAAZAQEBAQEBAQAAAAAAAAAAAAAAAwIBBAX/xAAdEQEBAQEAAgMBAAAAAAAAAAAAAQIRAzESIUET/9oADAMBAAIRAxEAPwDrAH0XzQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAe00zVPZiZ5a26nB3KtlE/XKHOu8aBInBXI/s8piWmuiaJ7cTHOMjsOViA64AAAAAAAAAAAAAAAAAAAAAAJ+F0f0ozv6v0+P1Z6OwuURXcjX4Ru4rBLW/yK5x+1jbtxbjKiIjkyBNQeVUxVGVURMcXoCBidHRMZ2NU/l8J5KyqOjOVW10SHj8L1tHSojtR+6FM7/KnrH7FSAqkAAAAAAAAAAAAAAAAAANuEtddfiPDbPKGpYaIo7VU8oZ1eRrM7VlGqNQCC4AAAAACm0hZ6rETlsnXHujLTS1Gdqmd1WXnCrXzexDU5QBpkAAAAAAAAAAAAAAAAWeiPhVfNH2VifomvK5VG+Iny/wCs79N49rMBBYAAAAABE0p+F/ypVCz0tX2KaeOfkrFsekd+wBtgAAAAAAAAAAAAAAAAZ2LnU3YqjwnzhgDroaKorpiadkxm9VOAxfUz0bnd3/l/0tonONSGpxfN7ABl0AAJnKNYrdIYzOJotTzn2h2TrlvEXGXuvvzMbNkcmkF4hQB1wAAAAAAAAAAAAAAAAAASMNjKrGqNdO6fZHHLOuy8XNrHUXNs5Tun+UiKoq2TDniJy2MXxtzyOimctrRdxdFrbVE8I1ypJnPaH8y+RLxOOqvRlR2afWUQG5OMW9AHXAAAAAAAAAAAAAAAAAAAZ2bNV6rK3H8Qs8Po+m3rudqf2s3UjUzarLdmq7P/AJ0zP2Srejaqu/MR6ytYjKNQnd1SYiDToymO9VVPLKGcaOo/V5pY58q18YiTo6jj5sKtGUz3aqo55SnB8qfGKq5oyqO5MT6Si3bNVr4lMx9l+TGca3Zus3Ec6Le/o+m58Pszw2eSsv2KrFWVyPr4SpNSp3NjWA0yAAAAAAAAAAAAAAJGDwk4ic51U79/CHmDw/8AUXOEbZ9l1RTFFMRTGUQxrXPpTOe/by3bi1RlbjKGQIqgAAAAAAADG5RFynKuM4ZAKfGYObE5066fWOaK6GqOlTlVsU2Nw39PX2e7OzhwVzrv1UtZ59xHAUTAAAAAAAAAACmOlVEU7Z1Cbou10rs1TsjZzly3kdk7eLDDWYsWoiPrO+W0HnegAAAAAAAAAAAAYX7UXrUxV4+k72YDnrlE265irbE5PFhpWzlMVRyn2V70S9iFnKAOsgAAAAAAAC60fb6GFp46/NS7XQ0R0aIiPCIhPyVTxx6AkqAAAAAAAAAAAAAA1Yu31uHqjhn9YULo3P3aehdqjdVMeqvjqfkjEBRIAAAAAAABlajO7T80fd0Cgs/Gp+an7r9LyK+MATUAAAAAAAAAAAAAAFHjIyxVfzLxR438XXz9lPH7Y8nppAVRAAAAf//Z" name="" />
            <StyledName>이름없음</StyledName>
          </FlexContainer>
        </StyledMembersProfileContainer>
      </StyledMemberListContainer>

      {/* 초대링크 */}
      <StyledInvitationLink onClick={handleCopyInviationCode}>
        <img src={Plus} alt="inivation-link" />
        <StyledInviteText>새로운 멤버 초대</StyledInviteText>
      </StyledInvitationLink>
    </StyledContainer>
  );
}

const StyledSettingIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;
`;

const StyledGroupListIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;
`;

const StyledGroupImage = styled.img`
  width: 8rem;
  height: 8rem;
  border-radius: 1.2rem;
`;

const StyledGroupTitle = styled.div`
  font-size: 1.6rem;
  margin-bottom: 1.2rem;
`;

const StyledGroupMemberCount = styled.div(({ theme }) => `
  font-size: 1.6rem;
  color: ${theme.colors.GRAY_400};
`);

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
  margin-bottom: 2.8rem;
`;

const StyledContent = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1.6rem;
`;

const StyledMenuHeader = styled.div`
width: 100%;
font-size: 2rem;
text-align: left;
margin-bottom: 2rem;
`;

const StyledGroupButton = styled(Link)<{ isDefaultGroup: boolean }>(
  ({ theme, isDefaultGroup }) => `
  width: 100%;
  font-size: 1.6rem;
  color: ${isDefaultGroup ? theme.colors.BLACK_100 : theme.colors.GRAY_400};
  text-align: left;
  
`
);

const StyledGroupIconGroup = styled.div`
  display: flex;
  align-items: flex-start;
  gap: 0.4rem;
  padding-right: 4rem;
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
    border-bottom-left-radius: 4rem;`};
`
);

// 멤버
const StyledMemberListContainer = styled.div`
  margin: 2.8rem 0;
`;

const StyledName = styled.div`
  font-size: 1.6rem;
`;

const StyledMembersProfileContainer = styled.div`
  overflow-y: auto;
  height: 40rem;
`;

// 초대링크
const StyledInviteText = styled.p`
  font-size: 2rem;
`;

export default Sidebar;
