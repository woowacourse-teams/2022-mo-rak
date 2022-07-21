import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import Progress from '../common/Progress/Progress';
import { PollInterface, PollItemResultType } from '../../types/poll';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import { getPollResult } from '../../api/poll';
import { GroupInterface, MemberInterface } from '../../types/group';
import { getGroupMembers } from '../../api/group';

interface Props {
  pollId: PollInterface['id'];
  groupCode: GroupInterface['code'];
}

// TODO: 비효율적이다...리팩토링
const getCurrentParticipants = (pollResult: Array<PollItemResultType>) => {
  const allParticipants = pollResult.map((pollItemResult) => pollItemResult.members).flat();
  const currentParticipants = allParticipants.map((participant) => participant.name);

  return new Set(currentParticipants).size;
};

function PollMainProgress({ pollId, groupCode }: Props) {
  const [pollResult, setPollResult] = useState<Array<PollItemResultType>>([]);
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);
  const totalParticipants = groupMembers.length;
  const currentParticipants = getCurrentParticipants(pollResult);

  useEffect(() => {
    const fetchPollResult = async (pollId: PollInterface['id']) => {
      try {
        if (groupCode) {
          const res = await getPollResult(pollId, groupCode);
          setPollResult(res);
        }
      } catch (err) {
        alert(err);
      }
    };

    fetchPollResult(pollId);
  }, []);

  useEffect(() => {
    const fetchGroupMembers = async () => {
      try {
        if (groupCode) {
          const res = await getGroupMembers(groupCode);

          setGroupMembers(res);
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
  font-size: 0.8rem;
`;

export default PollMainProgress;
