import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';

import Box from '../common/Box/Box';
import PollMainStatus from '../PollMainStatus/PollMainStatus';
import PollMainTitle from '../PollMainTitle/PollMainTitle';
import PollMainDetail from '../PollMainDetail/PollMainDetail';
import PollMainProgress from '../PollMainProgress/PollMainProgress';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import MarginContainer from '../common/MarginContainer/MarginContainer';
import Button from '../common/Button/Button';
import { getPolls } from '../../api/poll';
import { PollInterface } from '../../types/poll';
import PollMainButtonGroup from '../PollMainButtonGroup/PollMainButtonGroup';

function PollMainContainer() {
  const navigate = useNavigate();
  const theme = useTheme();
  const [polls, setPolls] = useState<Array<PollInterface>>([]);

  useEffect(() => {
    const fetchPolls = async () => {
      const res = await getPolls();
      setPolls(res);
    };

    try {
      fetchPolls();
    } catch (err) {
      alert(err);
    }
  }, []);

  // TODO: hook으로 뺄까?
  const handleNavigate = (location:string) => () => {
    navigate(location);
  };

  return (
    <>
      <Button width="8rem" color={theme.colors.WHITE_100} colorScheme={theme.colors.PURPLE_100} variant="filled" onClick={handleNavigate('/create')}>
        투표 생성하기
      </Button>
      {/* TODO: Wrapper가 아닌듯? 컴포넌트 나누고 고쳐도 될듯 */}
      <StyledWrapper>
        {polls ? polls.map((poll) => (
          <Box width="26.4rem" padding="2rem" minHeight="16.8rem">
            <FlexContainer justifyContent="end">
              <PollMainStatus status={poll.status} />
            </FlexContainer>
            {/* TODO: title prop으로 줄지? children으로 줄지? */}
            <PollMainTitle title={poll.title} />
            <PollMainProgress pollId={poll.id} />
            <MarginContainer margin="0 0 10px">
              {/* 'detail' 컴포넌트명 변경(전체 페이지 수정 필요) */}
              <PollMainDetail
                isAnonymous={poll.isAnonymous}
                allowedPollCount={poll.allowedPollCount}
              />
            </MarginContainer>
            <PollMainButtonGroup handleNavigate={handleNavigate} />
          </Box>
        )) : (<div>없습니다</div>)}
      </StyledWrapper>
    </>
  );
}

const StyledWrapper = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr); 
  column-gap: 30px;
  row-gap: 30px;
  margin-top: 1.6rem;
`;

export default PollMainContainer;
