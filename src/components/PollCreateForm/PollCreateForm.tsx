import React, { useState } from 'react';

import Box from '../common/Box/Box';
import Divider from '../common/Divider/Divider';
import MarginContainer from '../common/MarginContainer/MarginContainer';

import PollCreateFormInputGroup from '../PollCreateFormInputGroup/PollCreateFormInputGroup';
import PollCreateFormButtonGroup from '../PollCreateFormButtonGroup/PollCreateFormButtonGroup';
import PollCreateFormSubmitButton from '../PollCreateFormSubmitButton/PollCreateFormSubmitButton';
import PollCreateFormTitle from '../PollCreateFormTitle/PollCreateFormTitle';

// TODO: 추상화 레벨에 대해서 다시 돌아보기
function PollCreateForm() {
  const [pollTitle, setPollTitle] = useState('');
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [isMultiplePollCountAllowed, setIsMultiplePollCountAllowed] = useState(false);
  const [formInputs, setFormInputs] = useState(['', '']);

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    console.log(isAnonymous, isMultiplePollCountAllowed, pollTitle, formInputs);
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
