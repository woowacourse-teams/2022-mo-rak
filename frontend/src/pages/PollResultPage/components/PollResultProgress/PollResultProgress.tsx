import { StyledParticipantsStatus } from './PollResultProgress.styles';
import Progress from '@/components/Progress/Progress';
import { getPollResponse } from '@/types/poll';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import useGroupMembersContext from '@/hooks/useGroupMembersContext';

type Props = {
  currentParticipants: getPollResponse['count'];
};

function PollResultProgress({ currentParticipants }: Props) {
  const { groupMembers } = useGroupMembersContext();
  const totalParticipants = groupMembers.length;

  return (
    <FlexContainer flexDirection="column" alignItems="end">
      <Progress
        max={totalParticipants}
        value={currentParticipants}
        width="100%"
        padding="1.2rem 0"
      />
      <StyledParticipantsStatus>
        {currentParticipants}
        명/
        {totalParticipants}명
      </StyledParticipantsStatus>
    </FlexContainer>
  );
}

export default PollResultProgress;
