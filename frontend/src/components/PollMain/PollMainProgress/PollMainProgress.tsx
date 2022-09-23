import { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import Progress from '../../@common/Progress/Progress';
import { getPollResponse } from '../../../types/poll';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { GroupInterface, MemberInterface } from '../../../types/group';
import { getGroupMembers } from '../../../api/group';

interface Props {
  currentParticipants: getPollResponse['count'];
  groupCode: GroupInterface['code'];
}

function PollMainProgress({ currentParticipants, groupCode }: Props) {
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);
  const totalParticipants = groupMembers.length;

  useEffect(() => {
    const fetchGroupMembers = async () => {
      try {
        if (groupCode) {
          const res = await getGroupMembers(groupCode);

          setGroupMembers(res.data);
        }
      } catch (err) {
        if (err instanceof Error) {
          // TOOD: 모든 곳에서 401 대비를 해주는 게 맞을까?
          console.log(err);
        }
      }
    };

    fetchGroupMembers();
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

const StyledParticipantsStatus = styled.p`
  font-size: 1.6rem;
`;

export default PollMainProgress;
