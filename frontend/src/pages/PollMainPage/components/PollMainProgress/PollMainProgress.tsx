import { StyledParticipantsStatus } from '@/pages/PollMainPage/components/PollMainProgress/PollMainProgress.styles';

import FlexContainer from '@/components/FlexContainer/FlexContainer';
import Progress from '@/components/Progress/Progress';

import useGroupMembersContext from '@/hooks/useGroupMembersContext';

import { getPollResponse } from '@/types/poll';

type Props = {
  currentParticipants: getPollResponse['count'];
};

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
