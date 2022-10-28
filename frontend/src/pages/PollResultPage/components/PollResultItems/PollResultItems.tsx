import { useState } from 'react';
import { useTheme } from '@emotion/react';
import {
  StyledParticipantCount,
  StyledUserIcon,
  StyledCrownIcon,
  StyledCheckIcon,
  StyledUserCount,
  StyledSubject
} from './PollResultItems.styles';
import {
  Poll,
  PollItem,
  Members,
  getPollResultResponse,
  getPollItemsResponse
} from '../../../../types/poll';
import crownImg from '../../../../assets/crown.svg';
import checkImg from '../../../../assets/check.svg';
import userPurpleImg from '../../../../assets/user-purple.svg';
import userWhiteImg from '../../../../assets/user-white.svg';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import TextField from '../../../../components/TextField/TextField';
import PollResultParticipantModal from '../PollResultParticipantModal/PollResultParticipantModal';
import useModal from '../../../../hooks/useModal';

type Props = {
  status: Poll['status'];
  pollResult: getPollResultResponse;
  pollItems: getPollItemsResponse;
};

// TODO: 이렇게 함수를 밖에 꺼내놓는 게 낫나
const getWinningPollItemIds = (pollResult: getPollResultResponse) => {
  const pollItemCounts = pollResult.map((pollItem) => pollItem.count);
  const maxCount = Math.max(...pollItemCounts);

  return pollResult
    .filter((pollItem) => pollItem.count === maxCount)
    .map((pollItem) => pollItem.id);
};

const getSelectedPollItemIds = (pollItems: getPollItemsResponse) =>
  pollItems.filter((pollItem) => pollItem.isSelected).map((pollItem) => pollItem.id);

function PollResultItems({ status, pollResult, pollItems }: Props) {
  const theme = useTheme();
  const [activeMembers, setActiveMembers] = useState<Members>([]);
  const [activeSubject, setActiveSubject] = useState<PollItem['subject']>('');
  const selectedPollItemIds = getSelectedPollItemIds(pollItems);
  const winningPollItemIds = getWinningPollItemIds(pollResult);
  const [
    isPollResultParticipantModalVisible,
    openPollResultParticipantModal,
    closePollResultParticipantModal
  ] = useModal();

  const handleShowParticipant = (members: Members, subject: PollItem['subject']) => () => {
    const isParticipantExist = members.length >= 1;

    if (!isParticipantExist) return;

    setActiveMembers(members);
    setActiveSubject(subject);
    openPollResultParticipantModal();
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
              <StyledCheckIcon
                checked={selectedPollItemIds.includes(id)}
                src={checkImg}
                alt="check"
              />
            ) : (
              <StyledCrownIcon isWinningPollItem={isWinningPollItem} src={crownImg} alt="crown" />
            )}
            <StyledSubject
              status={status}
              isWinningPollItem={isWinningPollItem}
              aria-label={subject}
            >
              {subject}
            </StyledSubject>
            <StyledParticipantCount onClick={handleShowParticipant(members, subject)}>
              <StyledUserIcon
                src={status === 'CLOSED' && isWinningPollItem ? userWhiteImg : userPurpleImg}
                alt="user"
              />
              <StyledUserCount
                status={status}
                isWinningPollItem={isWinningPollItem}
                aria-label={`${subject}-count`}
              >
                {count}
              </StyledUserCount>
            </StyledParticipantCount>
          </TextField>
        );
      })}

      <PollResultParticipantModal
        isVisible={isPollResultParticipantModalVisible}
        close={closePollResultParticipantModal}
        participants={activeMembers}
        subject={activeSubject}
      />
    </FlexContainer>
  );
}

export default PollResultItems;
