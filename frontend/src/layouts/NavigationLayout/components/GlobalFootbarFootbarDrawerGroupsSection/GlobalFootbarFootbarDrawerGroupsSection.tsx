import { useNavigate } from 'react-router-dom';
import Plus from '../../../../assets/plus.svg';
import Leave from '../../../../assets/leave.svg';
import { leaveGroup } from '../../../../api/group';
import { Group } from '../../../../types/group';
import {
  StyledMenuHeader,
  StyledGroupsModalBox,
  StyledGroupProfile,
  StyledGroupFirstCharacter,
  StyledGroupName,
  StyledContainer,
  StyledGroup,
  StyledParticipateNewGroupButton,
  StyledCreateNewGroupButton,
  StyledButtonText,
  StyledLeaveGroupButton,
  StyledPlusImage,
  StyledLeaveImage
} from './GlobalFootbarFootbarDrawerGroupsSection.styles';

type Props = {
  groupCode: Group['code'];
  groups: Array<Group>;
  onClickMenu: (menu: string) => () => void;
  closeDrawer: () => void;
};

const getRandomPastelColor = () =>
  `hsl(${360 * Math.random()},${25 + 70 * Math.random()}%,${85 + 10 * Math.random()}%)`;

function GlobalFootbarFootbarDrawerGroupsSection({
  closeDrawer,
  onClickMenu,
  groupCode,
  groups
}: Props) {
  const navigate = useNavigate();
  const profileColor = getRandomPastelColor();

  const handleLeaveGroup = async () => {
    if (window.confirm('그룹을 나가시겠습니까?')) {
      await leaveGroup(groupCode);
      navigate('/init');
      closeDrawer();
    }
  };

  const handleNavigateGroup = (groupCode: Group['code'], groupName: Group['name']) => () => {
    if (confirm(`${groupName} 그룹으로 이동하시겠습니까?`)) {
      navigate(`groups/${groupCode}`);
      closeDrawer();
    }
  };

  return (
    <StyledContainer>
      <StyledMenuHeader>그룹</StyledMenuHeader>
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
    </StyledContainer>
  );
}
export default GlobalFootbarFootbarDrawerGroupsSection;
