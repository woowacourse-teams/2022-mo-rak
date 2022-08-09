import React, { useEffect, useState, ChangeEvent, ChangeEventHandler } from 'react';

import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import FlexContainer from '../../common/FlexContainer/FlexContainer';

import {
  PollInterface,
  PollItemInterface,
  SelectedPollItem,
  getPollItemsResponse
} from '../../../types/poll';

import { GroupInterface } from '../../../types/group';

import { getPollItems } from '../../../api/poll';
import TextField from '../../common/TextField/TextField';
import Radio from '../../common/Radio/Radio';
import Checkbox from '../../common/Checkbox/Checkbox';
import Input from '../../common/Input/Input';

interface Props {
  pollCode: PollInterface['code'];
  selectedPollItems: Array<SelectedPollItem>;
  allowedPollCount: PollInterface['allowedPollCount'];
  groupCode: GroupInterface['code'];
  handleSelectPollItems: (mode: string) => ChangeEventHandler<HTMLInputElement>;
  handleDescription: (pollId: number) => ChangeEventHandler<HTMLInputElement>;
}

function PollProgressItemGroup({
  pollCode,
  selectedPollItems,
  handleSelectPollItems,
  handleDescription,
  allowedPollCount,
  groupCode
}: Props) {
  const theme = useTheme();
  const [pollItems, setPollItems] = useState<getPollItemsResponse>([]);
  const getIsSelectedPollItem = (pollId: PollInterface['id']) =>
    selectedPollItems.some((selectedPollItem) => selectedPollItem.itemId === pollId);

  useEffect(() => {
    const fetchPollItems = async (pollCode: PollInterface['code']) => {
      try {
        if (groupCode) {
          const res = await getPollItems(pollCode, groupCode);
          setPollItems(res);
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
      {pollItems.map(({ id, subject }: PollItemInterface) => (
        <>
          <TextField
            colorScheme={theme.colors.PURPLE_100}
            padding="1.2rem 0"
            width="74.4rem"
            variant="outlined"
            borderRadius="10px"
          >
            {allowedPollCount >= 2 ? (
              <Checkbox
                id={String(id)}
                checked={getIsSelectedPollItem(id)}
                onChange={handleSelectPollItems('multiple')}
              >
                {subject}
              </Checkbox>
            ) : (
              <Radio
                id={String(id)}
                name={subject}
                checked={getIsSelectedPollItem(id)}
                onChange={handleSelectPollItems('single')}
              >
                {subject}
              </Radio>
            )}
          </TextField>

          <StyledDescription isSelected={getIsSelectedPollItem(id)}>
            <TextField
              colorScheme={theme.colors.PURPLE_100}
              width="74.4rem"
              variant="outlined"
              borderRadius="10px"
              padding="1.2rem 0"
            >
              <Input
                color={theme.colors.BLACK_100}
                fontSize="12px"
                placeholder="선택한 이유는?"
                onChange={handleDescription(id)}
                aria-label={subject}
              />
            </TextField>
          </StyledDescription>
        </>
      ))}
    </FlexContainer>
  );
}

const StyledDescription = styled.div<{ isSelected: boolean }>(
  ({ isSelected }) => `
  display: ${isSelected ? 'block' : 'none'};
`
);

export default PollProgressItemGroup;
