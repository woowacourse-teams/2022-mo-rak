import { CSSProperties } from 'react';
import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';
import Setting from '../../assets/setting.svg';
import Menu from '../../assets/menu.svg';
import Plus from '../../assets/plus.svg';
import Leave from '../../assets/leave.svg';
import FlexContainer from '../@common/FlexContainer/FlexContainer';
import { leaveGroup } from '../../api/group';
import { GroupInterface } from '../../types/group';
import useMenuDispatchContext from '../../hooks/useMenuDispatchContext';
import useMenuContext from '../../hooks/useMenuContext';


interface Props {
  groupCode: GroupInterface['code'];
  groups: Array<GroupInterface>;
  onClickMenu: (menu: string) => () => void; 
} 

const getRandomPastelColor = () =>
`hsl(${360 * Math.random()},${25 + 70 * Math.random()}%,${85 + 10 * Math.random()}%)`;

function SidebarGroupsMenu({ onClickMenu, groupCode, groups }: Props) {
  const navigate = useNavigate();
  const dispatch = useMenuDispatchContext();
  const { isVisibleGroupsModal } = useMenuContext();
  const profileColor = getRandomPastelColor();
  const currentGroup = groups.find((group) => group.code === groupCode);

  const handleLeaveGroup = async () => {
    if (window.confirm('그룹을 나가시겠습니까?')) {
      try {
        await leaveGroup(groupCode);
        navigate('/init');
      } catch (err) {
        alert(err);
      }
    }
  };

  const handleToggleGroupsModal = () => {
    dispatch({ type: 'TOGGLE_GROUPS_MODAL' });
  }

  const closeGroupsModal = () => {
    dispatch({ type: 'SET_IS_VISIBLE_GROUPS_MODAL', payload: false });
  }

  const handleNavigateGroup =
    (groupCode: GroupInterface['code'], groupName: GroupInterface['name']) => () => {
      if (confirm(`${groupName} 그룹으로 이동하시겠습니까?`)) {
        navigate(`groups/${groupCode}`);
        closeGroupsModal();
      }
    };

  return (
    <StyledGroupContainer>
      <StyledMenuHeader>그룹</StyledMenuHeader>
      <StyledGroupProfileAndGroupIcon>
        <FlexContainer gap="3rem">
          <StyledGroupProfile backgroundColor={profileColor}>
            <StyledGroupFirstCharacter>{currentGroup?.name[0]}</StyledGroupFirstCharacter>
          </StyledGroupProfile>
          <StyledGroupName>{currentGroup?.name}</StyledGroupName>
        </FlexContainer>
        <StyledGroupIconGroup>
          <StyledSettingIcon src={Setting} />
          <StyledGroupsModalIcon src={Menu} onClick={handleToggleGroupsModal} />
        </StyledGroupIconGroup>
      </StyledGroupProfileAndGroupIcon>

      <StyledGroupsModalContainer isVisible={isVisibleGroupsModal}>
        <StyledGroupsModalBox>
          {groups.map((group) => {
            const isCurrentGroup = groupCode === group.code;

            return (
            <StyledGroup
              key={group.code}
              onClick={handleNavigateGroup(group.code, group.name)}
              isActive={isCurrentGroup}
            >
              <StyledGroupProfile
                backgroundColor={isCurrentGroup ? profileColor : getRandomPastelColor()}
              >
                <StyledGroupFirstCharacter>{group?.name[0]}</StyledGroupFirstCharacter>
              </StyledGroupProfile>
              <StyledGroupName>{group.name}</StyledGroupName>
            </StyledGroup>
          )})}
        </StyledGroupsModalBox>

        <StyledParticipateNewGroupButton onClick={onClickMenu('participate')}>
          <StyledPlusImage src={Plus} alt="participate-new-group-button" />
          <StyledButtonText>새로운 그룹 참가</StyledButtonText>
        </StyledParticipateNewGroupButton>
        <StyledCreateNewGroupButton onClick={onClickMenu('create')}>
          <StyledPlusImage src={Plus} alt="create-new-group-button" />
          <StyledButtonText>새로운 그룹 생성</StyledButtonText>
        </StyledCreateNewGroupButton>
        <StyledLeaveGroupButton onClick={handleLeaveGroup}>
          <StyledLeaveImage src={Leave} alt="group-exit-button" />
          <StyledButtonText>그룹 나가기</StyledButtonText>
        </StyledLeaveGroupButton>
      </StyledGroupsModalContainer>
    </StyledGroupContainer>
  );
}

const StyledMenuHeader = styled.div`
  font-size: 1.6rem;
  text-align: left;
  margin-bottom: 2rem;
`;

const StyledGroupProfileAndGroupIcon = styled.div`
  display: flex;
  justify-content: space-between;
`;

const StyledGroupsModalContainer = styled.div<{ isVisible: boolean }>(
  ({ theme, isVisible }) => `
  position: absolute;
  right: -25.2rem;
  top: 4.4rem;
  visibility: hidden;
  width: 24rem;
  max-height: 55.2rem;
  border-radius: 1.2rem;
  background: ${theme.colors.WHITE_100};
  opacity: 0;
  transition: visibility 0s, opacity 0.2s ease-in-out;

  ${
    isVisible &&
    `
      visibility: visible;
      opacity: 1;
    `
  }
`
);

const StyledGroupsModalBox = styled.div`
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

const StyledGroupsModalIcon = styled.img`
  width: 2.4rem;
  cursor: pointer;

  &:hover {
    transform: scale(1.1, 1.1);
    transition: all 0.3s linear;
  }
`;

const StyledGroupProfile = styled.div<CSSProperties>(
  ({ backgroundColor }) => `
  display: flex;
  justify-content: center;
  align-items: center;
  width: 8rem;
  height: 8rem;
  border-radius: 1.2rem;
  font-family: 'Nanum Gothic', sans-serif;
  background: ${backgroundColor};
`
);

const StyledGroupFirstCharacter = styled.div(
  ({ theme }) => `
  color: ${theme.colors.WHITE_100};
  font-size: 4.8rem;
`
);

const StyledGroupName = styled.div`
  font-size: 1.6rem;
  margin-bottom: 1.2rem;
`;

const StyledGroupContainer = styled.div`
  position: relative;
  width: 100%;
  margin-bottom: 2.8rem;
`;

const StyledGroup = styled.div<{ isActive: boolean }>(
  ({ theme, isActive }) => `
  display: flex;
  gap: 2rem;
  align-items: center;
  padding: 2rem;
  text-decoration: none;
  margin: 1.2rem;
  color: ${theme.colors.BLACK_100};
  cursor: pointer;

  &:hover {
    background: ${theme.colors.GRAY_100};
    border-radius: 1.2rem;
    transition: all 0.2s linear;
  }

  ${
    isActive &&
    `
      background: ${theme.colors.GRAY_100}; 
      border-radius: 1.2rem;
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

const StyledParticipateNewGroupButton = styled.button`
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

const StyledCreateNewGroupButton = styled.button`
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

const StyledButtonText = styled.p`
  font-size: 2rem;
`;

const StyledLeaveGroupButton = styled.button`
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

const StyledPlusImage = styled.img`
  width: 2.4rem;
  height: 2.4rem;
`;

const StyledLeaveImage = styled.img`
  width: 2.4rem;
  height: 2.4rem;
`;

export default SidebarGroupsMenu;
