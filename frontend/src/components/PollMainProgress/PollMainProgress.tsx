import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import Progress from '../common/Progress/Progress';
import { PollInterface, PollItemResultType } from '../../types/poll';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import { getPollResult } from '../../api/poll';
import theme from '../../styles/theme';

interface Props {
  pollId: PollInterface['id'];
}

function PollMainProgress({ pollId }: Props) {
  const [pollResult, setPollResult] = useState<Array<PollItemResultType>>([]);
  const totalParticipants = pollResult.length; // TODO: 그룹 기능 추가되면, 업데이트할 것
  const currentParticipants = pollResult.reduce(
    (prev: number, cur: PollItemResultType) => prev + cur.count,
    0
  );

  useEffect(() => {
    const fetchPollResult = async (pollId: PollInterface['id']) => {
      const res = await getPollResult(pollId);
      setPollResult(res);
    };

    try {
      fetchPollResult(pollId);
    } catch (e) {
      alert(e);
    }
  }, []);

  return (
    <FlexContainer flexDirection="column" alignItems="end">
      <Progress
        max={totalParticipants}
        value={currentParticipants}
        width="100%"
      />
      {/* //TODO: prettier 설정 보기 */}
      <StyledParticipantsStatus>
        {currentParticipants}
        명/
        {totalParticipants}
        명
      </StyledParticipantsStatus>
    </FlexContainer>
  );
}

const StyledParticipantsStatus = styled.p`
  font-size: 0.8rem;
`;

export default PollMainProgress;
