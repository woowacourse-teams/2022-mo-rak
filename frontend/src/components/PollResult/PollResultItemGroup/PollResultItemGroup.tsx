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
  pollId: PollInterface['id'];
  status: PollInterface['status'];
  groupCode?: GroupInterface['code'];
  pollResult: Array<PollItemResultType>;
}

const getWinningPollItemIds = (pollResult: PollItemResultType[]) => {
  const pollItemCounts = pollResult.map((pollItem) => pollItem.count);
  const maxCount = Math.max(...pollItemCounts);

  return pollResult
    .filter((pollItem) => pollItem.count === maxCount)
    .map((pollItem) => pollItem.id);
};

// TODO: 타이핑 리팩토링
const getSelectedPollItemIds = (pollItems: Array<{ id: number; isSelected: boolean }>) =>
  pollItems.filter((pollItem) => pollItem.isSelected).map((pollItem) => pollItem.id);

// TODO: Props로 interface를 설정해줘서 type vs 직접 인자에 type 설정
// TODO: status에 따라 보여주는 컴포넌트가 달라지도록 분기처리를 해주는 것이 좋을까?
function PollResultItemGroup({ pollId, status, groupCode, pollResult }: Props) {
  const theme = useTheme();
  const [activePollItem, setActivePollItem] = useState(0); // TODO: 변수명 고민
  const [selectedPollItemIds, setSelectedPollItemIds] = useState<Array<PollItemInterface['id']>>(
    []
  );
  const winningPollItemIds = getWinningPollItemIds(pollResult);

  const handleShowParticipant = (pollId: PollInterface['id']) => () => {
    setActivePollItem(pollId);
  };

  useEffect(() => {
    const fetchPollItems = async (pollId: PollInterface['id']) => {
      try {
        if (groupCode) {
          const res = await getPollItems(pollId, groupCode);
          setSelectedPollItemIds(getSelectedPollItemIds(res));
        }
      } catch (err) {
        alert(err);
      }
    };

    if (pollId) {
      fetchPollItems(pollId);
    }
  }, []);

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      {pollResult?.map(({ id, subject, count, members }) => {
        const isWinningPollItem = winningPollItemIds.includes(id);

        return (
          <TextField
            variant={status === 'CLOSED' && isWinningPollItem ? 'filled' : 'outlined'}
            fontSize="1.6rem"
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
  right: 10px;
  top: 10px;
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
  top: 1rem;
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
`
);

const StyledSubject = styled.span<{ isWinningPollItem: boolean; status: PollInterface['status'] }>(
  ({ theme, isWinningPollItem, status }) => `
  color: ${
    status === 'CLOSED' && isWinningPollItem ? theme.colors.WHITE_100 : theme.colors.BLACK_100
  };
`
);

export default PollResultItemGroup;
