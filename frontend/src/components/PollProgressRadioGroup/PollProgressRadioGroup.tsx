import React, { useEffect, useState, Dispatch } from 'react';

import { useTheme } from '@emotion/react';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import Button from '../common/Button/Button';

import { PollInterface, PollItemInterface } from '../../types/poll';

import { getPollItems } from '../../api/poll';

interface Props {
  pollId: PollInterface['id'];
  selectedPollItem: PollItemInterface | undefined;
  setSelectedPollItem: Dispatch<React.SetStateAction<PollItemInterface | undefined>>;
}

const getIsSelectedPollItem = (pollItem: PollItemInterface, selectedPollItem: PollItemInterface) =>
  pollItem === selectedPollItem;

function PollProgressRadioGroup({ pollId, selectedPollItem, setSelectedPollItem }: Props) {
  const theme = useTheme();
  const [pollItems, setPollItems] = useState<Array<PollItemInterface>>();

  useEffect(() => {
    const fetchPollItems = async (pollId: PollInterface['id']) => {
      const res = await getPollItems(pollId);

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
      {/* // TODO: 반복되는 Button 제거 */}
      {/* TODO: Radio로 만들기 */}
      {pollItems?.map((pollItem: PollItemInterface) => {
        if (selectedPollItem) {
          const isSelectedPollItem = getIsSelectedPollItem(pollItem, selectedPollItem);

          return (
            <Button
              id={pollItem.id}
              colorScheme={theme.colors.PURPLE_100}
              width="74.4rem"
              height="3.6rem"
              variant={isSelectedPollItem ? 'filled' : 'outlined'}
              color={isSelectedPollItem ? theme.colors.WHITE_100 : theme.colors.BLACK_100}
              fontSize="1.6rem"
              onClick={() => setSelectedPollItem(pollItem)}
            >
              {pollItem.subject}
            </Button>
          );
        }

        return (
          <Button
            id={pollItem.id}
            colorScheme={theme.colors.PURPLE_100}
            width="74.4rem"
            height="3.6rem"
            variant="outlined"
            color={theme.colors.BLACK_100}
            fontSize="1.6rem"
            onClick={() => setSelectedPollItem(pollItem)}
          >
            {pollItem.subject}
          </Button>
        );
      })}
    </FlexContainer>
  );
}

export default PollProgressRadioGroup;
