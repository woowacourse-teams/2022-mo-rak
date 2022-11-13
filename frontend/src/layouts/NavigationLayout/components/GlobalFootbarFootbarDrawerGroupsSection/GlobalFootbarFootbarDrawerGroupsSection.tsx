import { useNavigate } from 'react-router-dom';
import plusImg from '@/assets/plus.svg';
import leaveImg from '@/assets/leave.svg';
import { leaveGroup } from '@/api/group';
import { Group } from '@/types/group';
import {
  StyledMenuHeader,
  StyledGroups,
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
} from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerGroupsSection/GlobalFootbarFootbarDrawerGroupsSection.styles';
import { CONFIRM_MESSAGE } from '@/constants/message';

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
    if (window.confirm(CONFIRM_MESSAGE.EXIT_GROUP)) {
      await leaveGroup(groupCode);
      navigate('/init');
      closeDrawer();
    }
  };

  const handleNavigateGroup = (groupCode: Group['code'], groupName: Group['name']) => () => {
    if (confirm(CONFIRM_MESSAGE.MOVE_GROUP(groupName))) {
      navigate(`groups/${groupCode}`);
      closeDrawer();
    }
  };

  return (
    <StyledContainer>
      <StyledMenuHeader>그룹</StyledMenuHeader>
      <StyledGroups>
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
      </StyledGroups>

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
    </StyledContainer>
  );
}
export default GlobalFootbarFootbarDrawerGroupsSection;
