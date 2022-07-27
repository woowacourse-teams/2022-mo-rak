import React, { useState, FormEvent, ChangeEvent } from 'react';

// TODO: 자동정렬 설정
import { useNavigate, useParams } from 'react-router-dom';
import Box from '../../common/Box/Box';
import Divider from '../../common/Divider/Divider';
import MarginContainer from '../../common/MarginContainer/MarginContainer';

import PollCreateFormInputGroup from '../PollCreateFormInputGroup/PollCreateFormInputGroup';
import PollCreateDetail from '../PollCreateDetail/PollCreateDetail';
import PollCreateFormSubmitButton from '../PollCreateFormSubmitButton/PollCreateFormSubmitButton';
import PollCreateFormTitleInput from '../PollCreateFormTitleInput/PollCreateFormTitleInput';

import { createPoll } from '../../../api/poll';
import { PollCreateType, PollInterface } from '../../../types/poll';

function PollCreateForm() {
  const navigate = useNavigate();
  const { groupCode } = useParams() as { groupCode: string };

  const [title, setTitle] = useState('');
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [isAllowedMultiplePollCount, setIsAllowedMultiplePollCount] = useState(false);
  const [pollItems, setPollItems] = useState(['', '']);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const allowedPollCount = isAllowedMultiplePollCount ? pollItems.length : 1;

    // TODO: PollCreateRequest?
    const pollData: PollCreateType = {
      title,
      allowedPollCount,
      isAnonymous,
      closedAt: '9999-12-31T11:59:59',
      subjects: pollItems
    };

    try {
      // TODO: 쿼리 적용

      const res = await createPoll(pollData, groupCode);
      const pollId = res.headers.get('location').split('polls/')[1];

      // TODO: 상수화
      navigate(`/groups/${groupCode}/poll/${pollId}/progress`);
    } catch (err) {
      // TODO: 에러 핸들링 고도화
      alert(err);
    }
  };

  const handleTitle = (e: ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  const handleAnonymous = (anonymousStatus: PollInterface['isAnonymous']) => () => {
    setIsAnonymous(anonymousStatus);
  };

  const handleAllowedMultiplePollCount = (isAllowedMultiplePollCountStatus: boolean) => () => {
    setIsAllowedMultiplePollCount(isAllowedMultiplePollCountStatus);
  };

  return (
    <Box width="84.4rem" padding="6.4rem 4.8rem 14rem">
      <form onSubmit={handleSubmit}>
        <MarginContainer margin="0 0 4rem 0">
          <PollCreateFormTitleInput title={title} onChange={handleTitle} />
          <Divider />
        </MarginContainer>
        <MarginContainer margin="0 0 1.6rem 0">
          <PollCreateDetail
            isAnonymous={isAnonymous}
            handleAnonymous={handleAnonymous}
            isAllowedMultiplePollCount={isAllowedMultiplePollCount}
            handleAllowedMultiplePollCount={handleAllowedMultiplePollCount}
          />
        </MarginContainer>
        <MarginContainer margin="0 0 4rem 0">
          <PollCreateFormInputGroup pollItems={pollItems} setPollItems={setPollItems} />
        </MarginContainer>
        <PollCreateFormSubmitButton />
      </form>
    </Box>
  );
}

export default PollCreateForm;
