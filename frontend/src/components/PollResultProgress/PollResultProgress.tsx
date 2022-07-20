import React from 'react';
import styled from '@emotion/styled';
import Progress from '../common/Progress/Progress';
import { PollItemResultType } from '../../types/poll';
import FlexContainer from '../common/FlexContainer/FlexContainer';

interface Props {
  pollResult: Array<PollItemResultType>;
}

function PollResultProgress({ pollResult }: Props) {
  const totalParticipants = pollResult.length; // TODO: 그룹 멤버수?
  const currentParticipants = pollResult.reduce(
    (prev: number, cur: PollItemResultType) => prev + cur.count,
    0
  );

  return (
    <FlexContainer flexDirection="column" alignItems="end">
      <Progress
        max={totalParticipants}
        value={currentParticipants}
        width="100%"
        padding="1.2rem 0"
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
  font-size: 1.2rem;
`;

export default PollResultProgress;
