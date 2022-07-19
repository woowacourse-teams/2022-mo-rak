import React, { useEffect, useState, CSSProperties } from 'react';
import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import { PollInterface, PollItemResultType } from '../../types/poll';
import { getPollResult } from '../../api/poll';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import Crown from '../../assets/crown.svg';

// import UserWhite from '../../assets/user_white.svg';
import UserPurple from '../../assets/user_purple.svg';
import UserWhite from '../../assets/user_white.svg';
import PollParticipantModal from '../PollParticipantModal/PollParticipantModal';
import TextField from '../common/TextField/TextField';

interface Props {
  pollId: PollInterface['id'];
  status: PollInterface['status'];
}

const getWinningPollItem = (pollItems: PollItemResultType[]) => {
  let maxCount = 0;
  let winningPollItemId = 0;

  // TODO: 메서드로 구현해보기 (reduce)
  pollItems.forEach((pollItem) => {
    if (pollItem.count > maxCount) {
      maxCount = pollItem.count;
      winningPollItemId = pollItem.id;
    }
  });

  return winningPollItemId;
};

// TODO: Props로 interface를 설정해줘서 type vs 직접 인자에 type 설정
function PollResultItemGroup({ pollId, status }: Props) {
  const theme = useTheme();
  const [pollItems, setPollItems] = useState<Array<PollItemResultType>>([]);
  const [activePollItem, setActivePollItem] = useState(0); // TODO: 변수명 고민
  const [winningPollItem, setWinningPollItem] = useState(0);

  useEffect(() => {
    const fetchPollItems = async (pollId: PollInterface['id']) => {
      const res = await getPollResult(pollId);

      await setPollItems(res);
      await setWinningPollItem(getWinningPollItem(res));
    };

    try {
      if (pollId) {
        fetchPollItems(pollId);
      }
    } catch (err) {
      alert(err);
    }
  }, []);

  const handleShowParticipant = (pollId: PollInterface['id']) => () => {
    setActivePollItem(pollId);
  };

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      { pollItems?.map(({ id, subject, count }) => (
        <TextField
          variant={status === 'CLOSED' && winningPollItem === id ? 'filled' : 'outlined'}
          fontSize="1.6rem"
          padding="1.2rem 0"
          borderRadius="15px"
          color={status === 'CLOSED' && winningPollItem === id ? theme.colors.WHITE_100 : theme.colors.BLACK_100}
          colorScheme={theme.colors.PURPLE_100}
        >
          <StyledCrownIcon isClosed={status === 'CLOSED'} isWinningPollItem={winningPollItem === id} src={Crown} alt="crown" />
          {subject}
          <StyledParticipantCount onClick={handleShowParticipant(id)}>
            <FlexContainer>
              <StyledUserIcon src={status === 'CLOSED' && winningPollItem === id ? UserWhite : UserPurple} alt="user" />
              <StyledUserCount
                color={status === 'CLOSED' && winningPollItem === id ? theme.colors.WHITE_100 : theme.colors.BLACK_100}
              >
                {count}
              </StyledUserCount>
            </FlexContainer>
            {(activePollItem === id) ? <PollParticipantModal participants={[{ id: 1, name: '우영우', profileUrl: 'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/06/PS22062800115.jpg' }, { id: 2, name: '태연', profileUrl: 'https://cdnweb01.wikitree.co.kr/webdata/editor/202202/03/img_20220203152221_f00e3cfa.webp' }]} /> : ''}
          </StyledParticipantCount>
        </TextField>
      ))}
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

const StyledCrownIcon = styled.img<CSSProperties & {
  isClosed: boolean;
  isWinningPollItem: boolean;
}>(({ isClosed, isWinningPollItem }) => `
  display: ${isClosed && isWinningPollItem ? 'inline' : 'none'};
  position: absolute;
  top: 0.8rem;
  left: 2rem;
  width: 2rem;
  height: 2rem;
`);

const StyledUserCount = styled.span<CSSProperties>((color) => `
  color: ${color};
`);

export default PollResultItemGroup;
