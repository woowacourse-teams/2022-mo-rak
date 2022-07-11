import React, { useEffect, useState } from 'react';
import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import Button from '../common/Button/Button';
import { PollInterface, PollItemInterface } from '../../types/poll';
import { getPollResult } from '../../api/poll';
import FlexContainer from '../common/FlexContainer/FlexContainer';

// import UserWhite from '../../assets/user_white.svg';
import UserPurple from '../../assets/user_purple.svg';

interface Props {
  pollId: PollInterface['id'];
}

// TODO: Props로 interface를 설정해줘서 type vs 직접 인자에 type 설정
function PollResultItemGroup({ pollId }: Props) {
  const theme = useTheme();
  const [pollItems, setPollItems] = useState<Array<PollItemInterface>>();

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
          <StyledParticipantCount>
            <FlexContainer>
              <StyledIcon src={UserPurple} alt={UserPurple} />
              <span>{pollItem.count}</span>
            </FlexContainer>
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
`;

const StyledIcon = styled.img`
  width: 1.6rem;
  height: 1.6rem;
`;
export default PollResultItemGroup;
