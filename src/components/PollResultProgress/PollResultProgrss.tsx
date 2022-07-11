import React from 'react';
import styled from '@emotion/styled';
import Progress from '../common/Progress/Progress';
import { PollResultInterface } from '../../types/poll';
import FlexContainer from '../common/FlexContainer/FlexContainer';

interface Props {
  pollResult: Array<PollResultInterface>;
}

function PollResultProgress({ pollResult }: Props) {
  const totalParticipants = pollResult.length;
  const currentParticipants = pollResult.reduce(
    (prev: number, cur: PollResultInterface) => prev + cur.count,
    0
  );

  return (
    <FlexContainer flexDirection="column" alignItems="end">
      <Progress
        max={totalParticipants}
        value={currentParticipants}
        width="73.6rem"
        height="1.6rem"
      />
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
  font-size: 1.2rem;
`;

export default PollResultProgress;
