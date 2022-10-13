import { useNavigate } from 'react-router-dom';
import Setting from '../../../../assets/setting.svg';
import Menu from '../../../../assets/menu.svg';
import Plus from '../../../../assets/plus.svg';
import Leave from '../../../../assets/leave.svg';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { leaveGroup } from '../../../../api/group';
import { GroupInterface } from '../../../../types/group';
import useMenuDispatchContext from '../../../../hooks/useMenuDispatchContext';
import useMenuContext from '../../../../hooks/useMenuContext';
import {
  StyledMenuHeader,
  StyledGroupsModalContainer,
  StyledGroupsModalBox,
  StyledSettingIcon,
  StyledGroupsModalIcon,
  StyledGroupProfile,
  StyledGroupFirstCharacter,
  StyledGroupName,
  StyledGroupContainer,
  StyledGroup,
  StyledGroupIconGroup,
  StyledParticipateNewGroupButton,
  StyledCreateNewGroupButton,
  StyledButtonText,
  StyledLeaveGroupButton,
  StyledPlusImage,
  StyledLeaveImage
} from './SidebarGroupsMenu.styles';

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
  };

  const closeGroupsModal = () => {
    dispatch({ type: 'SET_IS_VISIBLE_GROUPS_MODAL', payload: false });
  };

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
      <FlexContainer justifyContent="space-between">
        <FlexContainer gap="2rem">
          <StyledGroupProfile backgroundColor={profileColor}>
            <StyledGroupFirstCharacter>{currentGroup?.name[0]}</StyledGroupFirstCharacter>
          </StyledGroupProfile>
          <StyledGroupName>{currentGroup?.name}</StyledGroupName>
        </FlexContainer>
        <StyledGroupIconGroup>
          <StyledSettingIcon src={Setting} />
          <StyledGroupsModalIcon src={Menu} onClick={handleToggleGroupsModal} />
        </StyledGroupIconGroup>
      </FlexContainer>

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
            );
          })}
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
          <StyledLeaveImage src={Leave} alt="group-leave-button" />
          <StyledButtonText>그룹 나가기</StyledButtonText>
        </StyledLeaveGroupButton>
      </StyledGroupsModalContainer>
    </StyledGroupContainer>
  );
}
export default SidebarGroupsMenu;
