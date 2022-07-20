import React, { useEffect, useState } from 'react';
import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import Button from '../common/Button/Button';
import { PollInterface, PollResultInterface } from '../../types/poll';
import { getPollResult } from '../../api/poll';
import FlexContainer from '../common/FlexContainer/FlexContainer';

// import UserWhite from '../../assets/user_white.svg';
import UserPurple from '../../assets/user_purple.svg';
import PollParticipantModal from '../PollParticipantModal/PollParticipantModal';

interface Props {
  pollId: PollInterface['id'];
}

// TODO: Props로 interface를 설정해줘서 type vs 직접 인자에 type 설정
function PollResultItemGroup({ pollId }: Props) {
  const theme = useTheme();
  // [{"id":6,"count":1,"members":[],"subject":"a"},{"id":7,"count":0,"members":[],"subject":"b"}]
  const [pollItems, setPollItems] = useState<Array<PollResultInterface>>();
  const [clickedId, setClickedId] = useState(0);

  useEffect(() => {
    const fetchPollItems = async (pollId: PollInterface['id']) => {
      const res = await getPollResult(pollId);

      setPollItems(res);
    };

    try {
      if (pollId) {
        fetchPollItems(pollId);
      }
    } catch (err) {
      alert(err);
    }
  }, []);

  const handleShowParticipant = (pollId: number) => () => {
    setClickedId(pollId);
  };

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      {pollItems?.map((pollItem) => (
        <Button
          variant="outlined"
          height="3.6rem"
          fontSize="1.6rem"
          color={theme.colors.BLACK_100}
          colorScheme={theme.colors.PURPLE_100}
          disabled
        >
          {pollItem.subject}
          <StyledParticipantCount onClick={handleShowParticipant(pollItem.id)}>
            <FlexContainer>
              <StyledIcon src={UserPurple} alt={UserPurple} />
              <span>{pollItem.count}</span>
            </FlexContainer>
            {(clickedId === pollItem.id) ? <PollParticipantModal participants={[{ id: 1, name: '우영우', profileUrl: 'https://spnimage.edaily.co.kr/images/Photo/files/NP/S/2022/06/PS22062800115.jpg' }, { id: 2, name: '태연', profileUrl: 'https://cdnweb01.wikitree.co.kr/webdata/editor/202202/03/img_20220203152221_f00e3cfa.webp' }]} /> : ''}
          </StyledParticipantCount>
        </Button>
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

const StyledIcon = styled.img`
  width: 1.6rem;
  height: 1.6rem;
`;
export default PollResultItemGroup;
