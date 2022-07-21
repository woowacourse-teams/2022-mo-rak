import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';
import { useNavigate, useParams } from 'react-router-dom';

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
  const { groupCode } = useParams();
  const theme = useTheme();
  const [polls, setPolls] = useState<Array<PollInterface>>([]);

  useEffect(() => {
    const fetchPolls = async () => {
      if (groupCode) {
        const res = await getPolls(groupCode);
        setPolls(res);
      }
    };

    try {
      fetchPolls();
    } catch (err) {
      alert(err);
    }
  }, []);

  // TODO: hook으로 뺄까?
  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <>
      {/* TODO: refactor */}
      {polls && (
        <FlexContainer justifyContent="end">
          <Button
            width="8rem"
            color={theme.colors.WHITE_100}
            colorScheme={theme.colors.PURPLE_100}
            variant="filled"
            onClick={handleNavigate('create')}
          >
            투표 생성하기
          </Button>
        </FlexContainer>
      )}
      <StyledContainer>
        {/* status에따라, filter: grayscale(1) 주기 */}
        {polls ? (
          polls.map(({ status, title, id, isAnonymous, allowedPollCount }) => (
            <Box
              width="26.4rem"
              padding="2rem"
              minHeight="16.8rem"
              filter={status === 'CLOSED' ? 'grayscale(1)' : ''}
            >
              <FlexContainer justifyContent="end">
                <PollMainStatus status={status} />
              </FlexContainer>
              {/* TODO: title prop으로 줄지? children으로 줄지? */}
              <PollMainTitle title={title} />
              <PollMainProgress pollId={id} groupCode={groupCode} />
              <MarginContainer margin="0 0 10px">
                {/* 'detail' 컴포넌트명 변경(전체 페이지 수정 필요) */}
                <PollMainDetail isAnonymous={isAnonymous} allowedPollCount={allowedPollCount} />
              </MarginContainer>
              <PollMainButtonGroup pollId={id} handleNavigate={handleNavigate} status={status} />
            </Box>
          ))
        ) : (
          <div>없습니다</div>
        )}
      </StyledContainer>
    </>
  );
}

const StyledContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  column-gap: 30px;
  row-gap: 30px;
  margin-top: 1.6rem;
`;

export default PollMainContainer;
