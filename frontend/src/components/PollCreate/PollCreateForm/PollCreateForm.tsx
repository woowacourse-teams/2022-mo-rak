import { useState, FormEvent, ChangeEvent } from 'react';

// TODO: 자동정렬 설정
import { useNavigate, useParams } from 'react-router-dom';
import { AxiosError } from 'axios';
import Box from '../../@common/Box/Box';
import Divider from '../../@common/Divider/Divider';
import MarginContainer from '../../@common/MarginContainer/MarginContainer';

import PollCreateFormInputGroup from '../PollCreateFormInputGroup/PollCreateFormInputGroup';
import PollCreateDetail from '../PollCreateDetail/PollCreateDetail';
import PollCreateFormSubmitButton from '../PollCreateFormSubmitButton/PollCreateFormSubmitButton';
import PollCreateFormTitleInput from '../PollCreateFormTitleInput/PollCreateFormTitleInput';
import PollCreateCloseTimeInput from '../PollCreateCloseTimeInput/PollCreateCloseTimeInput';

import { createPoll } from '../../../api/poll';
import { createPollData, PollInterface } from '../../../types/poll';
import { GroupInterface } from '../../../types/group';
import useInput from '../../../hooks/useInput';

function PollCreateForm() {
  const navigate = useNavigate();
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };

  const [title, setTitle] = useState('');
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [isAllowedMultiplePollCount, setIsAllowedMultiplePollCount] = useState(false);
  const [pollItems, setPollItems] = useState(['', '']);
  const [closingDate, handleCloseDate] = useInput('');
  const [closingTime, handleCloseTime] = useInput('');

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const allowedPollCount = isAllowedMultiplePollCount ? pollItems.length : 1;

    const pollData: createPollData = {
      title,
      allowedPollCount,
      isAnonymous,
      closedAt: `${closingDate}T${closingTime}`,
      subjects: pollItems
    };

    try {
      // TODO: 쿼리 적용
      const res = await createPoll(pollData, groupCode);
      const pollCode = res.headers.location.split('polls/')[1];

      // TODO: 상수화
      navigate(`/groups/${groupCode}/poll/${pollCode}/progress`);
    } catch (err) {
      if (err instanceof AxiosError) {
        // TODO: 타이핑
        const codeNumber = err.response?.data.codeNumber;

        if (codeNumber === '4000') {
          alert('투표 마감시간은 현재보다 미래여야합니다');
        }
      }
      // TODO: 에러 핸들링 고도화
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
    <Box width="84.4rem" padding="4.8rem 4.8rem 14rem">
      <form onSubmit={handleSubmit}>
        <PollCreateCloseTimeInput
          closingDate={closingDate}
          closingTime={closingTime}
          onChangeDate={handleCloseDate}
          onChangeTime={handleCloseTime}
        />
        <MarginContainer margin="0 0 4rem 0">
          <PollCreateFormTitleInput title={title} onChange={handleTitle} />
          <Divider />
        </MarginContainer>
        <MarginContainer margin="0 0 1.6rem 0">
          <PollCreateDetail
            isAnonymous={isAnonymous}
            // NOTE: onClickAnonymous로 바꿔주는 게 어떨까?
            handleAnonymous={handleAnonymous}
            isAllowedMultiplePollCount={isAllowedMultiplePollCount}
            // TODO: is 붙여주는 게 더 말이 될듯
            // NOTE: onClickAllowedMultiplePollCount로 바꿔주는 게 어떨까?
            handleAllowedMultiplePollCount={handleAllowedMultiplePollCount}
          />
        </MarginContainer>
        <MarginContainer margin="0 0 4rem 0">
          {/* NOTE: setPollItems를 넘겨주는 게 맞을까? 다른 것들은 handle함수를 만들어서 내려주는 데 일관성이 여기서 깨지는 것 같다  */}
          <PollCreateFormInputGroup pollItems={pollItems} setPollItems={setPollItems} />
        </MarginContainer>
        <PollCreateFormSubmitButton />
      </form>
    </Box>
  );
}

export default PollCreateForm;
