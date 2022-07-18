import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import Progress from '../common/Progress/Progress';
import { PollInterface, PollItemResultType } from '../../types/poll';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import { getPollResult } from '../../api/poll';
import theme from '../../styles/theme';

interface Props {
  pollId: PollInterface['id'];
  status: PollInterface['status'];
}

function PollMainProgress({ pollId, status }: Props) {
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
        accentColor={status === 'OPEN' ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
      />
      {/* //TODO: prettier 설정 보기 */}
      <StyledParticipantsStatus status={status}>
        {currentParticipants}
        명/
        {totalParticipants}
        명
      </StyledParticipantsStatus>
    </FlexContainer>
  );
}

const StyledParticipantsStatus = styled.p<{
  status: string;
}>(
  ({ status }) => `
  font-size: 0.8rem;
  color: ${status === 'OPEN' ? theme.colors.BLACK_100 : theme.colors.GRAY_400}
`
);

export default PollMainProgress;
