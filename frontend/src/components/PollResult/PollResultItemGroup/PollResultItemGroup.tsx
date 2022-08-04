import React, { useEffect, useState, CSSProperties } from 'react';
import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import { PollInterface, PollItemInterface, PollItemResultType } from '../../../types/poll';
import { getPollItems } from '../../../api/poll';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import Crown from '../../../assets/crown.svg';
import Check from '../../../assets/check.svg';

import UserPurple from '../../../assets/user-purple.svg';
import UserWhite from '../../../assets/user-white.svg';
import PollParticipantModal from '../PollParticipantModal/PollParticipantModal';
import TextField from '../../common/TextField/TextField';
import { GroupInterface } from '../../../types/group';

interface Props {
  pollCode: PollInterface['code'];
  status: PollInterface['status'];
  groupCode: GroupInterface['code'];
  pollResult: Array<PollItemResultType>;
}

const getWinningPollItemIds = (pollResult: PollItemResultType[]) => {
  const pollItemCounts = pollResult.map((pollItem) => pollItem.count);
  const maxCount = Math.max(...pollItemCounts);

  return pollResult
    .filter((pollItem) => pollItem.count === maxCount)
    .map((pollItem) => pollItem.id);
};

// TODO: 타이핑 리팩토링, poll에 관한 원형 타입들 한 번 손봐야할듯
const getSelectedPollItemIds = (pollItems: Array<{ id: number; isSelected: boolean }>) =>
  pollItems.filter((pollItem) => pollItem.isSelected).map((pollItem) => pollItem.id);

function PollResultItemGroup({ pollCode, status, groupCode, pollResult }: Props) {
  const theme = useTheme();
  const [activePollItem, setActivePollItem] = useState(0);
  const [selectedPollItemIds, setSelectedPollItemIds] = useState<Array<PollItemInterface['id']>>(
    []
  );
  const winningPollItemIds = getWinningPollItemIds(pollResult);

  const handleShowParticipant = (pollId: PollInterface['id']) => () => {
    setActivePollItem(pollId);
  };

  useEffect(() => {
    const fetchPollItems = async (pollCode: PollInterface['code']) => {
      try {
        if (groupCode) {
          const res = await getPollItems(pollCode, groupCode);
          setSelectedPollItemIds(getSelectedPollItemIds(res));
        }
      } catch (err) {
        alert(err);
      }
    };

    if (pollCode) {
      fetchPollItems(pollCode);
    }
  }, []);

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      {pollResult?.map(({ id, subject, count, members }) => {
        const isWinningPollItem = winningPollItemIds.includes(id);

        return (
          <TextField
            variant={status === 'CLOSED' && isWinningPollItem ? 'filled' : 'outlined'}
            padding="1.2rem 0"
            borderRadius="15px"
            colorScheme={theme.colors.PURPLE_100}
          >
            {status === 'OPEN' ? (
              <StyledCheckIcon checked={selectedPollItemIds.includes(id)} src={Check} alt="check" />
            ) : (
              <StyledCrownIcon isWinningPollItem={isWinningPollItem} src={Crown} alt="crown" />
            )}
            <StyledSubject status={status} isWinningPollItem={isWinningPollItem}>
              {subject}
            </StyledSubject>
            <StyledParticipantCount onClick={handleShowParticipant(id)}>
              <FlexContainer>
                <StyledUserIcon
                  src={status === 'CLOSED' && isWinningPollItem ? UserWhite : UserPurple}
                  alt="user"
                />
                <StyledUserCount status={status} isWinningPollItem={isWinningPollItem}>
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

const StyledParticipantCount = styled.div`
  position: absolute;
  right: 1.2rem;
  top: 1.2rem;
  cursor: pointer;
`;

const StyledUserIcon = styled.img`
  width: 1.6rem;
  height: 1.6rem;
`;

const StyledCrownIcon = styled.img<
  CSSProperties & {
    isWinningPollItem: boolean;
  }
>(
  ({ isWinningPollItem }) => `
  display: ${isWinningPollItem ? 'block' : 'none'};
  position: absolute;
  top: 1.2rem;
  left: 2rem;
  width: 2rem;
  height: 2rem;
`
);

const StyledCheckIcon = styled.img<
  CSSProperties & {
    checked: boolean;
  }
>(
  ({ checked }) => `
  display: ${checked ? 'block' : 'none'};
  position: absolute;
  top: 0.8rem;
  left: 2rem;
  width: 2rem;
  height: 2rem;
`
);

const StyledUserCount = styled.span<{
  isWinningPollItem: boolean;
  status: PollInterface['status'];
}>(
  ({ theme, status, isWinningPollItem }) => `
  color: ${
  status === 'CLOSED' && isWinningPollItem ? theme.colors.WHITE_100 : theme.colors.BLACK_100
};
  font-size: 1.6rem;
`
);

const StyledSubject = styled.span<{ isWinningPollItem: boolean; status: PollInterface['status'] }>(
  ({ theme, isWinningPollItem, status }) => `
  color: ${
  status === 'CLOSED' && isWinningPollItem ? theme.colors.WHITE_100 : theme.colors.BLACK_100
};
  font-size: 1.6rem;
`
);

export default PollResultItemGroup;
