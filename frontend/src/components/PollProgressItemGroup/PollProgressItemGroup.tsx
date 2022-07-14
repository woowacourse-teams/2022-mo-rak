import React, { useEffect, useState, ChangeEventHandler, ChangeEvent } from 'react';

import { useTheme } from '@emotion/react';
import FlexContainer from '../common/FlexContainer/FlexContainer';

import { PollInterface, PollItemInterface } from '../../types/poll';

import { getPollItems } from '../../api/poll';
import TextField from '../common/TextField/TextField';
import Radio from '../common/Radio/Radio';
import Checkbox from '../common/Checkbox/Checkbox';

type PollItemIds = Array<PollItemInterface['id']>;

interface Props {
  pollId: PollInterface['id'];
  selectedPollItems: PollItemIds;
  allowedPollCount: PollInterface['allowedPollCount'];
  handleSelectPollItem: (mode: string) => (e: ChangeEvent<HTMLInputElement>) => void;
}

function PollProgressItemGroup({
  pollId,
  selectedPollItems,
  handleSelectPollItem,
  allowedPollCount
}: Props) {
  const theme = useTheme();
  const [pollItems, setPollItems] = useState<Array<PollItemInterface>>([]);

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
      {pollItems.map((pollItem: PollItemInterface) => {
        console.log('');

        return (
          <TextField
            colorScheme={theme.colors.PURPLE_100}
            width="74.4rem"
            height="3.6rem"
            variant="outlined"
            borderRadius="10px"
          >
            {/* TODO: 상수화 */}
            {allowedPollCount >= 2 ? (
              <Checkbox
                id={String(pollItem.id)}
                checked={selectedPollItems.includes(pollItem.id)}
                onChange={handleSelectPollItem('multiple')}
              >
                {pollItem.subject}
              </Checkbox>
            ) : (
              <Radio
                id={String(pollItem.id)}
                name={pollItem.subject}
                checked={selectedPollItems.includes(pollItem.id)}
                onChange={handleSelectPollItem('single')}
              >
                {pollItem.subject}
              </Radio>
            )}
          </TextField>
        );
      })}
    </FlexContainer>
  );
}

export default PollProgressItemGroup;
