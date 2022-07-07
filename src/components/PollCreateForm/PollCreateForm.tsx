import React, { useState, FormEvent, ChangeEvent, useContext } from 'react';

// TODO: 자동정렬 설정
import { useNavigate } from 'react-router-dom';
import Box from '../common/Box/Box';
import Divider from '../common/Divider/Divider';
import MarginContainer from '../common/MarginContainer/MarginContainer';

import PollCreateFormInputGroup from '../PollCreateFormInputGroup/PollCreateFormInputGroup';
import PollCreateFormButtonGroup from '../PollCreateFormButtonGroup/PollCreateFormButtonGroup';
import PollCreateFormSubmitButton from '../PollCreateFormSubmitButton/PollCreateFormSubmitButton';
import PollCreateFormTitleInput from '../PollCreateFormTitleInput/PollCreateFormTitleInput';

import { createPoll } from '../../api/poll';
import { PollContextStore } from '../../contexts/PollContext';
import { PollCreateType } from '../../types/poll';

// TODO: 추상화 레벨에 대해서 다시 돌아보기
function PollCreateForm() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [isMultiplePollCountAllowed, setIsMultiplePollCountAllowed] = useState(false);
  const [formInputs, setFormInputs] = useState(['', '']);

  // TODO: 왜 desturcuring이 안될까?
  // TODO: naming 생각해보자
  const pollContext = useContext(PollContextStore);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const allowedPollCount = isMultiplePollCountAllowed ? formInputs.length : 1;

    const pollData: PollCreateType = {
      title,
      allowedPollCount,
      isAnonymous,
      closedAt: '9999-12-31T11:59:59',
      subjects: formInputs
    };

    try {
      // TODO: 쿼리 적용
      const res = await createPoll(pollData);
      const pollId = res.headers.get('location').split('polls/')[1];

      pollContext?.setPollId(pollId);
      // TODO: 상수화
      navigate('/progress');
    } catch (err) {
      // TODO: 에러 핸들링 고도화
      alert(err);
    }
  };

  const handleTitle = (e: ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
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
          <PollCreateFormTitleInput title={title} onChange={handleTitle} />
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
