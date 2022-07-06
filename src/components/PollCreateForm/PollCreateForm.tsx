import React, { useState } from 'react';

import Box from '../common/Box/Box';
import Divider from '../common/Divider/Divider';
import MarginContainer from '../common/MarginContainer/MarginContainer';

import PollCreateFormInputGroup from '../PollCreateFormInputGroup/PollCreateFormInputGroup';
import PollCreateFormButtonGroup from '../PollCreateFormButtonGroup/PollCreateFormButtonGroup';
import PollCreateFormSubmitButton from '../PollCreateFormSubmitButton/PollCreateFormSubmitButton';
import PollCreateFormTitle from '../PollCreateFormTitle/PollCreateFormTitle';

import { createPoll } from '../../api/poll';

export interface PollCreateFormDataInterface {
  title: string;
  allowedPollCount: number;
  isAnonymous: boolean;
  closedAt: string;
  subjects: string[];
}

// TODO: 추상화 레벨에 대해서 다시 돌아보기
function PollCreateForm() {
  const [pollTitle, setPollTitle] = useState('');
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [isMultiplePollCountAllowed, setIsMultiplePollCountAllowed] = useState(false);
  const [formInputs, setFormInputs] = useState(['', '']);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const allowedPollCount = isMultiplePollCountAllowed ? formInputs.length : 1;

    // TODO: 변수명 생각해보자!
    const data: PollCreateFormDataInterface = {
      title: pollTitle,
      allowedPollCount,
      isAnonymous,
      closedAt: '9999-12-31T11:59:59',
      subjects: formInputs
    };

    try {
      // TODO: 쿼리 적용 필요
      await createPoll(data);
    } catch (err) {
      // TODO: 에러 핸들링 더 잘해줘야해~
      console.log(err);
    }
  };

  const handlePollTitle = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPollTitle(e.target.value);
  };

  const handleAnonymous = (anonymousStatus: boolean) => () => {
    setIsAnonymous(anonymousStatus);
  };

  const handleMultiplePollCountAllowed = (isMultiplePollCountAllowedStatus: boolean) => () => {
    setIsMultiplePollCountAllowed(isMultiplePollCountAllowedStatus);
  };

  return (
    <Box width="84.4rem" minHeight="65.2rem" padding="6.4rem 4.8rem">
      <form onSubmit={handleSubmit}>
        <MarginContainer margin="0 0 4rem 0">
          <PollCreateFormTitle pollTitle={pollTitle} handlePollTitle={handlePollTitle} />
          <Divider />
        </MarginContainer>
        <MarginContainer margin="0 0 1.6rem 0">
          <PollCreateFormButtonGroup
            isAnonymous={isAnonymous}
            handleAnonymous={handleAnonymous}
            isMultiplePollCountAllowed={isMultiplePollCountAllowed}
            handleMultiplePollCountAllowed={handleMultiplePollCountAllowed}
          />
        </MarginContainer>
        <MarginContainer margin="0 0 4rem 0">
          <PollCreateFormInputGroup formInputs={formInputs} setFormInputs={setFormInputs} />
        </MarginContainer>
        <PollCreateFormSubmitButton />
      </form>
    </Box>
  );
}

export default PollCreateForm;
