import { ChangeEventHandler } from 'react';

import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';

import { PollInterface, SelectedPollItem, getPollItemsResponse } from '../../../types/poll';

import TextField from '../../@common/TextField/TextField';
import Radio from '../../@common/Radio/Radio';
import Checkbox from '../../@common/Checkbox/Checkbox';
import Input from '../../@common/Input/Input';

interface Props {
  pollItems: getPollItemsResponse;
  selectedPollItems: Array<SelectedPollItem>;
  allowedPollCount: PollInterface['allowedPollCount'];
  onChangeCheckbox: ChangeEventHandler<HTMLInputElement>;
  onChangeRadio: ChangeEventHandler<HTMLInputElement>;
  onChangeText: (pollId: number) => ChangeEventHandler<HTMLInputElement>;
}

const getSelectedPollItem = (
  pollId: PollInterface['id'],
  selectedPollItems: Array<SelectedPollItem>
) => selectedPollItems.find((selectedPollItem) => selectedPollItem.id === pollId);

function PollProgressItemGroup({
  pollItems,
  selectedPollItems,
  onChangeCheckbox,
  onChangeRadio,
  onChangeText,
  allowedPollCount
}: Props) {
  const theme = useTheme();

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      {pollItems.map(({ id, subject }) => {
        const selectedPollItem = getSelectedPollItem(id, selectedPollItems);
        const isSelectedPollItem = !!selectedPollItem;

        return (
          <>
            <TextField
              colorScheme={theme.colors.PURPLE_100}
              padding="1.2rem 0"
              width="74.4rem"
              variant="outlined"
              borderRadius="10px"
            >
              {allowedPollCount >= 2 ? (
                <Checkbox id={String(id)} checked={isSelectedPollItem} onChange={onChangeCheckbox}>
                  {subject}
                </Checkbox>
              ) : (
                <Radio
                  id={String(id)}
                  name={subject}
                  checked={isSelectedPollItem}
                  onChange={onChangeRadio}
                >
                  {subject}
                </Radio>
              )}
            </TextField>

            <StyledDescription isVisible={isSelectedPollItem}>
              <TextField
                colorScheme={theme.colors.PURPLE_100}
                width="74.4rem"
                variant="outlined"
                borderRadius="10px"
                padding="1.2rem 0"
              >
                <Input
                  color={theme.colors.BLACK_100}
                  fontSize="1.2rem"
                  placeholder="선택한 이유는?"
                  value={selectedPollItem?.description}
                  onChange={onChangeText(id)}
                  aria-label={subject}
                />
              </TextField>
            </StyledDescription>
          </>
        );
      })}
    </FlexContainer>
  );
}

const StyledDescription = styled.div<{ isVisible: boolean }>(
  ({ isVisible }) => `
  display: ${isVisible ? 'block' : 'none'};
`
);

export default PollProgressItemGroup;
