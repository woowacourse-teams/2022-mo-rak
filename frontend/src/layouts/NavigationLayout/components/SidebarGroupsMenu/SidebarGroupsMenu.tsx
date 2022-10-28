import { useNavigate } from 'react-router-dom';
import settingImg from '../../../../assets/setting.svg';
import menuImg from '../../../../assets/menu.svg';
import plusImg from '../../../../assets/plus.svg';
import leaveImg from '../../../../assets/leave.svg';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { leaveGroup } from '../../../../api/group';
import { Group } from '../../../../types/group';
import useNavigationBarDispatchContext from '../../../../hooks/useNavigationBarDispatchContext';
import useNavigationBarContext from '../../../../hooks/useNavigationBarContext';
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

type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
  onClickMenu: (menu: string) => () => void;
};

const getRandomPastelColor = () =>
  `hsl(${360 * Math.random()},${25 + 70 * Math.random()}%,${85 + 10 * Math.random()}%)`;

function SidebarGroupsMenu({ onClickMenu, groupCode, groups }: Props) {
  const navigate = useNavigate();
  const navigationBarDispatch = useNavigationBarDispatchContext();
  const { isGroupsModalVisible } = useNavigationBarContext();
  const profileColor = getRandomPastelColor();
  const currentGroup = groups.find((group) => group.code === groupCode);

  const handleLeaveGroup = async () => {
    if (window.confirm('그룹을 나가시겠습니까?')) {
      await leaveGroup(groupCode);
      navigate('/init');
    }
  };

  const handleToggleGroupsModal = () => {
    navigationBarDispatch({ type: 'TOGGLE_GROUPS_MODAL' });
  };

  const closeGroupsModal = () => {
    navigationBarDispatch({ type: 'SET_IS_GROUPS_MODAL_VISIBLE', payload: false });
  };

  const handleNavigateGroup = (groupCode: Group['code'], groupName: Group['name']) => () => {
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
          <StyledSettingIcon src={settingImg} />
          <StyledGroupsModalIcon src={menuImg} onClick={handleToggleGroupsModal} />
        </StyledGroupIconGroup>
      </FlexContainer>

      <StyledGroupsModalContainer isVisible={isGroupsModalVisible}>
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
          <StyledPlusImage src={plusImg} alt="participate-new-group-button" />
          <StyledButtonText>새로운 그룹 참가</StyledButtonText>
        </StyledParticipateNewGroupButton>
        <StyledCreateNewGroupButton onClick={onClickMenu('create')}>
          <StyledPlusImage src={plusImg} alt="create-new-group-button" />
          <StyledButtonText>새로운 그룹 생성</StyledButtonText>
        </StyledCreateNewGroupButton>
        <StyledLeaveGroupButton onClick={handleLeaveGroup}>
          <StyledLeaveImage src={leaveImg} alt="group-leave-button" />
          <StyledButtonText>그룹 나가기</StyledButtonText>
        </StyledLeaveGroupButton>
      </StyledGroupsModalContainer>
    </StyledGroupContainer>
  );
}
export default SidebarGroupsMenu;
