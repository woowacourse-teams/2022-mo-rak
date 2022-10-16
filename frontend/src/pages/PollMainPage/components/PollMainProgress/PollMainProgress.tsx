import { StyledParticipantsStatus } from './PollMainProgress.styles';
import Progress from '../../../../components/Progress/Progress';
import { getPollResponse } from '../../../../types/poll';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import useGroupMembersContext from '../../../../hooks/useGroupMembersContext';

interface Props {
  currentParticipants: getPollResponse['count'];
}

function PollMainProgress({ currentParticipants }: Props) {
  const { groupMembers } = useGroupMembersContext();
  const totalParticipants = groupMembers.length;

  return (
    <FlexContainer flexDirection="column" alignItems="end">
      <Progress max={totalParticipants} value={currentParticipants} width="100%" />
      {/* //TODO: prettier 설정 보기 */}
      <StyledParticipantsStatus>
        {currentParticipants}
        명/
        {totalParticipants}명
      </StyledParticipantsStatus>
    </FlexContainer>
  );
}

export default PollMainProgress;
