import React, { useEffect, useState, ChangeEvent } from 'react';

import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import FlexContainer from '../common/FlexContainer/FlexContainer';

import { PollInterface, PollItemInterface, SelectedPollItemInterface } from '../../types/poll';

import { GroupInterface } from '../../types/group';

import { getPollItems } from '../../api/poll';
import TextField from '../common/TextField/TextField';
import Radio from '../common/Radio/Radio';
import Checkbox from '../common/Checkbox/Checkbox';
import Input from '../common/Input/Input';

type SelectedPollItemsType = Array<SelectedPollItemInterface>;

interface Props {
  pollId: PollInterface['id'];
  selectedPollItems: SelectedPollItemsType;
  allowedPollCount: PollInterface['allowedPollCount'];
  handleSelectPollItem: (mode: string) => (e: ChangeEvent<HTMLInputElement>) => void;
  handleDescription: (pollId: number) => (e: ChangeEvent<HTMLInputElement>) => void;
  groupCode?: GroupInterface['code'];
}

function PollProgressItemGroup({
  pollId,
  selectedPollItems,
  handleSelectPollItem,
  handleDescription,
  allowedPollCount,
  groupCode
}: Props) {
  const theme = useTheme();
  const [pollItems, setPollItems] = useState<Array<PollItemInterface>>([]);

  useEffect(() => {
    const fetchPollItems = async (pollId: PollInterface['id']) => {
      try {
        if (groupCode) {
          const res = await getPollItems(pollId, groupCode);
          setPollItems(res);
        }
      } catch (err) {
        alert(err);
      }
    };

    if (pollId) {
      fetchPollItems(pollId);
    }
  }, []);

  const getIsSelectedPollItem = (pollId: number) => {
    // TODO: 리팩터링 (메서드 찾아보기)
    for (const selectedPollItem of selectedPollItems) {
      if (selectedPollItem.itemId === pollId) {
        return true;
      }
    }

    return false;
  };

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
            {/* TODO: 상수화 */}
            {allowedPollCount >= 2 ? (
              <Checkbox
                id={String(id)}
                checked={getIsSelectedPollItem(id)}
                onChange={handleSelectPollItem('multiple')}
              >
                {subject}
              </Checkbox>
            ) : (
              <Radio
                id={String(id)}
                name={subject}
                checked={getIsSelectedPollItem(id)}
                onChange={handleSelectPollItem('single')}
              >
                {subject}
              </Radio>
            )}
          </TextField>

          {/* description */}
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
