import { useState } from 'react';
import { useTheme } from '@emotion/react';
import {
  StyledParticipantCount,
  StyledUserIcon,
  StyledCrownIcon,
  StyledCheckIcon,
  StyledUserCount,
  StyledSubject
} from './PollResultItemGroup.styles';
import {
  PollInterface,
  PollItemInterface,
  getPollResultResponse,
  getPollItemsResponse
} from '../../../../types/poll';
import Crown from '../../../../assets/crown.svg';
import Check from '../../../../assets/check.svg';

import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import UserPurple from '../../../../assets/user-purple.svg';
import UserWhite from '../../../../assets/user-white.svg';
import PollParticipantModal from '../PollParticipantModal/PollParticipantModal';
import TextField from '../../../../components/TextField/TextField';

interface Props {
  status: PollInterface['status'];
  pollResult: getPollResultResponse;
  pollItems: getPollItemsResponse;
}

const getWinningPollItemIds = (pollResult: getPollResultResponse) => {
  const pollItemCounts = pollResult.map((pollItem) => pollItem.count);
  const maxCount = Math.max(...pollItemCounts);

  return pollResult
    .filter((pollItem) => pollItem.count === maxCount)
    .map((pollItem) => pollItem.id);
};

const getSelectedPollItemIds = (pollItems: getPollItemsResponse) =>
  pollItems.filter((pollItem) => pollItem.isSelected).map((pollItem) => pollItem.id);

function PollResultItemGroup({ status, pollResult, pollItems }: Props) {
  const theme = useTheme();
  const [activePollItem, setActivePollItem] = useState(0);
  const selectedPollItemIds = getSelectedPollItemIds(pollItems);
  const winningPollItemIds = getWinningPollItemIds(pollResult);

  // TODO: 뭐지? 살펴보기
  const handleShowParticipant = (pollItemId: PollItemInterface['id']) => () => {
    setActivePollItem(pollItemId);
  };

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      {pollResult?.map(({ id, subject, count, members }) => {
        const isWinningPollItem = winningPollItemIds.includes(id);

        return (
          <TextField
            key={id}
            variant={status === 'CLOSED' && isWinningPollItem ? 'filled' : 'outlined'}
            padding="1.2rem 0"
            borderRadius="1.6rem"
            colorScheme={theme.colors.PURPLE_100}
            aria-label={`poll-result-${subject}`}
          >
            {status === 'OPEN' ? (
              <StyledCheckIcon checked={selectedPollItemIds.includes(id)} src={Check} alt="check" />
            ) : (
              <StyledCrownIcon isWinningPollItem={isWinningPollItem} src={Crown} alt="crown" />
            )}
            <StyledSubject
              status={status}
              isWinningPollItem={isWinningPollItem}
              aria-label={subject}
            >
              {subject}
            </StyledSubject>
            <StyledParticipantCount onClick={handleShowParticipant(id)}>
              <FlexContainer>
                <StyledUserIcon
                  src={status === 'CLOSED' && isWinningPollItem ? UserWhite : UserPurple}
                  alt="user"
                />
                <StyledUserCount
                  status={status}
                  isWinningPollItem={isWinningPollItem}
                  aria-label={`${subject}-count`}
                >
                  {count}
                </StyledUserCount>
              </FlexContainer>
              {activePollItem === id && <PollParticipantModal participants={members} />}
            </StyledParticipantCount>
          </TextField>
        );
      })}
    </FlexContainer>
  );
}

export default PollResultItemGroup;
