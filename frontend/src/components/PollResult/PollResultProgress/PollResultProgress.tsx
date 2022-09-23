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

function PollResultProgress({ currentParticipants, groupCode }: Props) {
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
          console.log(err);
        }
      }
    };

    fetchGroupMembers();
  }, []);

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

const StyledParticipantsStatus = styled.p`
  font-size: 1.2rem;
`;

export default PollResultProgress;
