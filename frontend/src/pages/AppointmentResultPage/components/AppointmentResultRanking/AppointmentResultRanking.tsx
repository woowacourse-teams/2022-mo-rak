import { MouseEventHandler, useState, useEffect } from 'react';

import Crown from '../../../../assets/crown.svg';
import { AppointmentRecommendation } from '../../../../types/appointment';
import { GroupInterface, MemberInterface } from '../../../../types/group';
import { getGroupMembers } from '../../../../api/group';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { getFormattedDateTime } from '../../../../utils/date';
import {
  StyledResultBox,
  StyledResultText,
  StyledCrownIcon,
  StyledRank
} from './AppointmentResultRanking.styles';

interface Props {
  groupCode: GroupInterface['code'];
  appointmentRecommendation: Array<AppointmentRecommendation>;
  // TODO: clicked보다는 selected가 더 맞지 않을까?
  clickedRecommendation: number;
  onClickRank: (idx: number) => MouseEventHandler<HTMLDivElement>;
}

function AppointmentResultRanking({
  groupCode,
  appointmentRecommendation,
  clickedRecommendation,
  onClickRank
}: Props) {
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);
  const totalParticipants = groupMembers.length;

  useEffect(() => {
    (async () => {
      const res = await getGroupMembers(groupCode);
      setGroupMembers(res.data);
    })();
  }, [groupCode]);

  return (
    <StyledResultBox>
      {appointmentRecommendation.map(
        (
          // TODO: destructuring에 관하여 일관성 살펴보기
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
                <StyledCrownIcon src={Crown} alt="crown" />
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
