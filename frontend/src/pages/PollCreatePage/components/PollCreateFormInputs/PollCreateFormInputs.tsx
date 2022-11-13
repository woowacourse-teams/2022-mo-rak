import { Dispatch, SetStateAction, MouseEvent, ChangeEvent, memo } from 'react';

import { useTheme } from '@emotion/react';

import { StyledDeleteIcon } from '@/pages/PollCreatePage/components/PollCreateFormInputs/PollCreateFormInputs.styles';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import Input from '@/components/Input/Input';
import TextField from '@/components/TextField/TextField';
import Button from '@/components/Button/Button';

import binImg from '@/assets/bin.svg';
import { PollItem } from '@/types/poll';
import { POLL_ERROR } from '@/constants/errorMessage';
import { CONFIRM_MESSAGE } from '@/constants/message';

type Props = {
  // TODO: pollItems 괜찮을까? subjects가 아닐까?
  pollItems: Array<PollItem['subject']>;
  setPollItems: Dispatch<SetStateAction<Array<PollItem['subject']>>>;
};

function PollCreateFormInputs({ pollItems, setPollItems }: Props) {
  const theme = useTheme();

  const handleAddPollItem = (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    // TODO: 상수화
    if (pollItems.length >= 10) {
      alert(POLL_ERROR.EXCEED_MAX_ITEM_COUNT);

      return;
    }

    setPollItems([...pollItems, '']);
  };

  const handleDeletePollItem = (targetIdx: number) => () => {
    // TODO: 상수화
    if (window.confirm(CONFIRM_MESSAGE.DELETE_POLL_ITEM)) {
      // TODO: 상수화
      if (pollItems.length === 2) {
        alert(POLL_ERROR.UNDER_MIN_ITEM_COUNT);
        return;
      }

      const newPollItems = [...pollItems].filter((_, idx) => idx !== targetIdx);

      setPollItems(newPollItems);
    }
  };

  const handleChange = (targetIdx: number) => (e: ChangeEvent<HTMLInputElement>) => {
    const newPollItems = [...pollItems];

    newPollItems[targetIdx] = e.target.value;

    setPollItems(newPollItems);
  };

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      {pollItems.map((pollItem, idx) => (
        // TODO: key를 넣어줘야한다.
        // eslint-disable-next-line react/jsx-key
        <TextField
          variant="outlined"
          borderRadius="10px"
          padding="1.2rem 10rem"
          position="relative"
          colorScheme={theme.colors.PURPLE_100}
        >
          <FlexContainer alignItems="center">
            <Input
              id={pollItem}
              value={pollItem}
              color={theme.colors.BLACK_100}
              fontSize="1.6rem"
              placeholder="선택항목을 입력해주세요!"
              onChange={handleChange(idx)}
              aria-label={`poll-input${idx}`}
              required
            />
            <StyledDeleteIcon src={binImg} alt="bin" onClick={handleDeletePollItem(idx)} />
          </FlexContainer>
        </TextField>
      ))}
      <Button
        variant="filled"
        colorScheme={theme.colors.PURPLE_100}
        fontSize="2rem"
        onClick={handleAddPollItem}
        type="button"
      >
        +
      </Button>
    </FlexContainer>
  );
}

export default memo(PollCreateFormInputs, (prev, next) => prev.pollItems === next.pollItems);
