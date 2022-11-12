import { StyledParticipantsStatus } from '@/pages/AppointmentMainPage/components/AppointmentMainProgress/AppointmentMainProgress.styles';

import FlexContainer from '@/components/FlexContainer/FlexContainer';
import Progress from '@/components/Progress/Progress';

import useGroupMembersContext from '@/hooks/useGroupMembersContext';

type Props = {
  count: number;
};

function AppointmentMainProgress({ count }: Props) {
  const { groupMembers } = useGroupMembersContext();
  const totalParticipants = groupMembers.length;
  const currentParticipants = count;

  return (
    <FlexContainer flexDirection="column" alignItems="end">
      <Progress max={totalParticipants} value={currentParticipants} width="100%" />
      {/* // TODO: prettier 설정 보기 */}
      <StyledParticipantsStatus>
        {currentParticipants}
        명/
        {totalParticipants}명
      </StyledParticipantsStatus>
    </FlexContainer>
  );
}

export default AppointmentMainProgress;
