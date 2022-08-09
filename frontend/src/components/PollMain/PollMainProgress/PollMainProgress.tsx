import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import Progress from '../../common/Progress/Progress';
import { PollInterface, getPollResultResponse } from '../../../types/poll';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import { getPollResult } from '../../../api/poll';
import { GroupInterface, MemberInterface } from '../../../types/group';
import { getGroupMembers } from '../../../api/group';

interface Props {
  pollCode: PollInterface['code'];
  groupCode: GroupInterface['code'];
}

// TODO: 재미로 리팩토링 해봐~ 심심할때
const getCurrentParticipants = (pollResult: getPollResultResponse) => {
  const allParticipants = pollResult.map((pollItemResult) => pollItemResult.members).flat();
  const currentParticipants = allParticipants.map((participant) => participant.name);

  return new Set(currentParticipants).size;
};

function PollMainProgress({ pollCode, groupCode }: Props) {
  const [pollResult, setPollResult] = useState<getPollResultResponse>([]);
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);
  const totalParticipants = groupMembers.length;
  const currentParticipants = getCurrentParticipants(pollResult);

  useEffect(() => {
    const fetchPollResult = async (pollCode: PollInterface['code']) => {
      try {
        if (groupCode) {
          const res = await getPollResult(pollCode, groupCode);
          setPollResult(res);
        }
      } catch (err) {
        alert(err);
      }
    };

    fetchPollResult(pollCode);
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
