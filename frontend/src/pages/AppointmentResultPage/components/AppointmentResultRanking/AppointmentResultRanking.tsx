import { MouseEventHandler } from 'react';

import crownImg from '@/assets/crown.svg';
import { AppointmentRecommendation } from '@/types/appointment';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import { getFormattedDateTime } from '../../../../utils/date';
import {
  StyledResultBox,
  StyledResultText,
  StyledCrownIcon,
  StyledRank
} from './AppointmentResultRanking.styles';
import useGroupMembersContext from '@/hooks/useGroupMembersContext';

type Props = {
  appointmentRecommendation: Array<AppointmentRecommendation>;
  // TODO: clicked보다는 selected가 더 맞지 않을까?
  clickedRecommendation: number;
  onClickRank: (idx: number) => MouseEventHandler<HTMLDivElement>;
};

function AppointmentResultRanking({
  appointmentRecommendation,
  clickedRecommendation,
  onClickRank
}: Props) {
  const { groupMembers } = useGroupMembersContext();
  const totalParticipants = groupMembers.length;

  return (
    <StyledResultBox>
      {appointmentRecommendation.map(
        (
          {
            rank,
            recommendStartDateTime,
            recommendEndDateTime,
            availableMembers
          }: AppointmentRecommendation,
          idx
        ) => (
          <StyledRank
            key={`${recommendStartDateTime}-${recommendEndDateTime}`}
            onClick={onClickRank(idx)}
            // TODO: isClicked?
            isClicked={idx === clickedRecommendation}
            aria-label={`appointment-result-${rank}`}
          >
            <FlexContainer justifyContent="space-between" alignItems="center">
              {/* TODO: 상수화 */}
              {rank === 1 ? (
                <StyledCrownIcon src={crownImg} alt="crown" />
              ) : (
                // TODO: Text라는 suffix에 대해서 일관성 살펴보기
                <StyledResultText>{rank}</StyledResultText>
              )}
              <StyledResultText>
                {getFormattedDateTime(recommendStartDateTime)}
                <br />~{getFormattedDateTime(recommendEndDateTime)}
              </StyledResultText>
              <StyledResultText>
                {availableMembers.length}/{totalParticipants}명 가능
              </StyledResultText>
            </FlexContainer>
          </StyledRank>
        )
      )}
    </StyledResultBox>
  );
}

export default AppointmentResultRanking;
