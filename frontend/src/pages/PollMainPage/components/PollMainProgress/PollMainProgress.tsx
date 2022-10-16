import { useEffect, useState } from 'react';
import { StyledParticipantsStatus } from './PollMainProgress.styles';
import Progress from '../../../../components/Progress/Progress';
import { getPollResponse } from '../../../../types/poll';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { Group, Member } from '../../../../types/group';
import { getGroupMembers } from '../../../../api/group';

type Props = {
  currentParticipants: getPollResponse['count'];
  groupCode: Group['code'];
};

function PollMainProgress({ currentParticipants, groupCode }: Props) {
  const [groupMembers, setGroupMembers] = useState<Array<Member>>([]);
  const totalParticipants = groupMembers.length;

  // TODO: 네트워크 요청 줄이기
  useEffect(() => {
    (async () => {
      const res = await getGroupMembers(groupCode);
      setGroupMembers(res.data);
    })();
  }, [groupCode]);

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
