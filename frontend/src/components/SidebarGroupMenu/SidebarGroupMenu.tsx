import React, { useState, useEffect, CSSProperties } from 'react';
import styled from '@emotion/styled';
import { Link, useNavigate } from 'react-router-dom';
import Setting from '../../assets/setting.svg';
import Menu from '../../assets/menu.svg';
import Plus from '../../assets/plus.svg';
import Leave from '../../assets/leave.svg';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import { leaveGroup } from '../../api/group';
import { GroupInterface } from '../../types/group';
import { useMenuDispatchContext, useMenuContext } from '../../context/MenuProvider';

interface Props {
  groupCode: GroupInterface['code'];
  groups: Array<GroupInterface>;
  onClickCreateMenu: () => void;
  onClickParticipateMenu: () => void;
}

function SidebarGroupMenu({ onClickCreateMenu, onClickParticipateMenu, groupCode, groups }: Props) {
  const [nowGroup, setGroup] = useState<GroupInterface>();
  const [profileColor, setProfileColor] = useState('');

  const dispatch = useMenuDispatchContext();
  const { isVisibleGroups } = useMenuContext();
  const navigate = useNavigate();

  const handleLeaveGroup = async () => {
    // TODO: 이후에 모달창으로 변경하기
    if (window.confirm('그룹을 나가시겠습니까?')) {
      try {
        await leaveGroup(groupCode);
        navigate('/init');
      } catch (err) {
        alert(err);
      }
    }
  };

  const handleToggleisVisibleGroups = () => {
    dispatch({ type: 'SET_SHOW_GROUP_LIST', payload: !isVisibleGroups });
  };

  const getRandomPastelColor = () => `hsl(${360 * Math.random()},${
    25 + 70 * Math.random()}%,${
    85 + 10 * Math.random()}%)`;

  useEffect(() => {
    const nowGroup = groups.find((group) => group.code === groupCode);
    setGroup(nowGroup);
  }, [groups, groupCode]);

  useEffect(() => {
    setProfileColor(getRandomPastelColor());
  }, [nowGroup]);

  return (
    <StyledGroupContainer>
      <StyledMenuHeader>그룹</StyledMenuHeader>

      <FlexContainer justifyContent="space-between">
        <FlexContainer gap="2rem">
          <StyledGroupProfile backgroundColor={profileColor}>
            <StyledGroupFirstName>{nowGroup && nowGroup.name[0]}</StyledGroupFirstName>
          </StyledGroupProfile>
          <FlexContainer flexDirection="column">
            <StyledGroupTitle>{nowGroup && nowGroup.name}</StyledGroupTitle>
          </FlexContainer>
        </FlexContainer>

        <StyledGroupIconGroup>
          {/* TODO: setting 아이콘은, host만 보이도록 해줘야한다. */}
          <StyledSettingIcon src={Setting} />
          <StyledGroupListIcon src={Menu} onClick={handleToggleisVisibleGroups} />
        </StyledGroupIconGroup>
      </FlexContainer>

      {/* group list */}
      <StyledGroupListBox isVisible={isVisibleGroups}>
        <StyledGroupListContainer>
          {groups.map((group) => (
            <StyledGroupList to={`groups/${group.code}`} isNowGroup={groupCode === group.code}>
              <StyledGroupProfile
                backgroundColor={groupCode === group.code ? profileColor : getRandomPastelColor()}
              >
                <StyledGroupFirstName>{group && group.name[0]}</StyledGroupFirstName>
              </StyledGroupProfile>
              <FlexContainer flexDirection="column">
                <StyledGroupTitle>{group.name}</StyledGroupTitle>
              </FlexContainer>
            </StyledGroupList>
          ))}
        </StyledGroupListContainer>
        <StyledParticipateNewGroup onClick={onClickParticipateMenu}>
          <img src={Plus} alt="participate-new-group-button" />
          <StyledGroupText>새로운 그룹 참가</StyledGroupText>
        </StyledParticipateNewGroup>
        <StyledCreateNewGroup>
          <img src={Plus} alt="create-new-group-button" />
          <StyledGroupText onClick={onClickCreateMenu}>새로운 그룹 생성</StyledGroupText>
        </StyledCreateNewGroup>
        <StyledLeaveGroup onClick={handleLeaveGroup}>
          <img src={Leave} alt="group-exit-button" />
          <StyledGroupText>그룹 나가기</StyledGroupText>
        </StyledLeaveGroup>
      </StyledGroupListBox>
    </StyledGroupContainer>
  );
}

const StyledMenuHeader = styled.div`
  width: 100%;
  font-size: 2rem;
  text-align: left;
  margin-bottom: 2rem;
`;

const StyledGroupListBox = styled.div<{isVisible: boolean}>(({ theme, isVisible }) => `
  visibility: hidden;
  opacity: 0;
  transition: visibility 0s, opacity 0.2s ease-in-out;
  width: 24rem;
  background: ${theme.colors.WHITE_100};
  position: absolute;
  right: -25.2rem;
  top: 4.4rem;
  border-radius: 12px;
  max-height: 55.2rem;

  ${isVisible && `
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

const StyledGroupProfile = styled.div<CSSProperties>(({ backgroundColor }) => `
  font-family: 'Nanum Gothic', sans-serif;
  width: 8rem;
  height: 8rem;
  border-radius: 1.2rem;
  background: ${backgroundColor};
  display: flex;
  justify-content: center;
  align-items: center;
`);

const StyledGroupFirstName = styled.div(({ theme }) => `
  color: ${theme.colors.WHITE_100};
  font-size: 4.8rem;
`);

const StyledGroupListImage = styled.img`
  width: 5.2rem;
  height: 5.2rem;
  border-radius: 0.8rem;
`;

const StyledGroupTitle = styled.div`
  font-size: 1.6rem;
  margin-bottom: 1.2rem;
`;

const StyledGroupContainer = styled.div`
position: relative;
  width: 100%;
  margin-bottom: 2.8rem;
`;

const StyledGroupList = styled(Link)<{ isNowGroup: boolean }>(
  ({ theme, isNowGroup }) => `
  display: flex;
  gap: 2rem;
  padding: 2rem;
  text-decoration: none;
  margin: 1.2rem;
  color: ${theme.colors.BLACK_100};

  ${isNowGroup
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

const StyledLeaveGroup = styled.button`
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

export default SidebarGroupMenu;
