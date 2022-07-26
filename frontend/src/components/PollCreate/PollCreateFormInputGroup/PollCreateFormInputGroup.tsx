import React, { Dispatch, SetStateAction, MouseEvent, ChangeEvent } from 'react';

import { useTheme } from '@emotion/react';

import styled from '@emotion/styled';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import Input from '../../common/Input/Input';
import TextField from '../../common/TextField/TextField';
import Button from '../../common/Button/Button';

import Bin from '../../../assets/bin.svg';

interface Props {
  // TODO: 타입을 재사용 할 수는 없을까?
  pollItems: Array<string>;
  setPollItems: Dispatch<SetStateAction<Array<string>>>;
}

function PollCreateFormInputGroup({ pollItems, setPollItems }: Props) {
  const theme = useTheme();

  const handleAddPollItem = (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    if (pollItems.length >= 10) {
      alert('최대 10개의 선택항목만 가능합니다');

      return;
    }

    setPollItems([...pollItems, '']);
  };

  const handleDeletePollItem = (targetIdx: number) => () => {
    // TODO: 상수화
    // TODO: 2개이하면 삭제되지 않도록 해줘야함
    if (window.confirm('해당 항목을 삭제하시겠습니까?')) {
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
              fontSize="1.2rem"
              placeholder="선택항목을 입력해주세요!"
              onChange={handleChange(idx)}
              required
            />
            <DeleteIcon src={Bin} alt="bin" onClick={handleDeletePollItem(idx)} />
          </FlexContainer>
        </TextField>
      ))}
      <Button
        variant="filled"
        colorScheme={theme.colors.PURPLE_100}
        color={theme.colors.WHITE_100}
        fontSize="2rem"
        onClick={handleAddPollItem}
        type="button"
      >
        +
      </Button>
    </FlexContainer>
  );
}

const DeleteIcon = styled.img`
  position: absolute;
  right: 1rem;
  cursor: pointer;
`;

export default PollCreateFormInputGroup;
