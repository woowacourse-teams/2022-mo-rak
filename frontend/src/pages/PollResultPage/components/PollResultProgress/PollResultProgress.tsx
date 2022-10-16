import { useEffect, useState } from 'react';
import { StyledParticipantsStatus } from './PollResultProgress.styles';
import Progress from '../../../../components/Progress/Progress';
import { getPollResponse } from '../../../../types/poll';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { Group, Member } from '../../../../types/group';
import { getGroupMembers } from '../../../../api/group';

type Props = {
  currentParticipants: getPollResponse['count'];
  groupCode: Group['code'];
};

function PollResultProgress({ currentParticipants, groupCode }: Props) {
  const [groupMembers, setGroupMembers] = useState<Array<Member>>([]);
  const totalParticipants = groupMembers.length;

  useEffect(() => {
    (async () => {
      const res = await getGroupMembers(groupCode);
      setGroupMembers(res.data);
    })();
  }, [groupCode]);

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
