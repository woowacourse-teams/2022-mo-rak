import React, { Dispatch, SetStateAction, MouseEvent, ChangeEvent } from 'react';

import { useTheme } from '@emotion/react';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import Input from '../common/Input/Input';
import Button from '../common/Button/Button';

import Bin from '../../assets/bin.svg';

interface Props {
  formInputs: Array<string>;
  setFormInputs: Dispatch<SetStateAction<Array<string>>>;
}

// TODO: 왜 비제어 컴포넌트일때는 안됐을까? 이거 비제어로 해야되긴한다...
function PollCreateFormInputGroup({ formInputs, setFormInputs }: Props) {
  const theme = useTheme();

  const handleAddInput = (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    if (formInputs.length >= 10) {
      alert('최대 10개의 선택항목만 가능합니다');

      return;
    }

    setFormInputs([...formInputs, '']);
  };

  const handleDeleteInput = (targetIdx: number) => () => {
    if (window.confirm('해당 항목을 삭제하시겠습니까?')) {
      const newFormInputs = [...formInputs].filter((_, idx) => idx !== targetIdx);

      setFormInputs(newFormInputs);
    }
  };

  const handleChange = (targetIdx: number) => (e: ChangeEvent<HTMLInputElement>) => {
    const newFormInputs = [...formInputs];

    newFormInputs[targetIdx] = e.target.value;

    setFormInputs(newFormInputs);
  };

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      {formInputs.map((formInput, idx) => (
        <Input
          value={formInput}
          color={theme.colors.BLACK_100}
          colorScheme={theme.colors.PURPLE_100}
          height="3.6rem"
          borderRadius="10px"
          variant="outlined"
          fontSize="1rem"
          placeholder="선택항목을 입력해주세요!"
          icon={Bin}
          onChange={handleChange(idx)}
          onClickIcon={handleDeleteInput(idx)}
          id={formInput}
          required
        />
      ))}
      <Button
        variant="filled"
        colorScheme={theme.colors.PURPLE_100}
        color={theme.colors.WHITE_100}
        fontSize="2rem"
        height="4rem"
        onClick={handleAddInput}
        type="button"
      >
        +
      </Button>
    </FlexContainer>
  );
}

export default PollCreateFormInputGroup;
