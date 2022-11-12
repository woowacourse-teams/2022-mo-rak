import { ChangeEventHandler } from 'react';

import { StyledDescription } from '@/pages/PollProgressPage/components/PollProgressItems/PollProgressItems.styles';

import Checkbox from '@/components/Checkbox/Checkbox';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import Input from '@/components/Input/Input';
import Radio from '@/components/Radio/Radio';
import TextField from '@/components/TextField/TextField';

import { Poll, SelectedPollItem, getPollItemsResponse } from '@/types/poll';
import { useTheme } from '@emotion/react';

type Props = {
  pollItems: getPollItemsResponse;
  selectedPollItems: Array<SelectedPollItem>;
  allowedPollCount: Poll['allowedPollCount'];
  onChangeCheckbox: ChangeEventHandler<HTMLInputElement>;
  onChangeRadio: ChangeEventHandler<HTMLInputElement>;
  onChangeText: (pollId: number) => ChangeEventHandler<HTMLInputElement>;
};

// TODO: 이렇게 밖으로 빼는 게 맞나
const getSelectedPollItem = (pollId: Poll['id'], selectedPollItems: Array<SelectedPollItem>) =>
  selectedPollItems.find((selectedPollItem) => selectedPollItem.id === pollId);

function PollProgressItems({
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
              variant="outlined"
              borderRadius="10px"
            >
              {allowedPollCount >= 2 ? (
                <Checkbox
                  id={String(id)}
                  checked={isSelectedPollItem}
                  onChange={onChangeCheckbox}
                  aria-label={subject}
                >
                  {subject}
                </Checkbox>
              ) : (
                <Radio
                  id={String(id)}
                  name={subject}
                  checked={isSelectedPollItem}
                  aria-label={subject}
                  onChange={onChangeRadio}
                >
                  {subject}
                </Radio>
              )}
            </TextField>

            <StyledDescription isVisible={isSelectedPollItem}>
              <TextField
                colorScheme={theme.colors.PURPLE_100}
                variant="outlined"
                borderRadius="10px"
                padding="1.2rem"
              >
                <Input
                  color={theme.colors.BLACK_100}
                  fontSize="1.6rem"
                  placeholder="선택한 이유는?"
                  value={selectedPollItem?.description ?? ''}
                  onChange={onChangeText(id)}
                  aria-label={`${subject}-description`}
                />
              </TextField>
            </StyledDescription>
          </>
        );
      })}
    </FlexContainer>
  );
}

export default PollProgressItems;
