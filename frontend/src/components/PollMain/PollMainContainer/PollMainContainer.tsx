import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';
import { useNavigate, useParams } from 'react-router-dom';

import Box from '../../@common/Box/Box';
import PollMainStatus from '../PollMainStatus/PollMainStatus';
import PollMainDetail from '../PollMainDetail/PollMainDetail';
import PollMainProgress from '../PollMainProgress/PollMainProgress';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import MarginContainer from '../../@common/MarginContainer/MarginContainer';

import { getPolls } from '../../../api/poll';
import { getPollsResponse } from '../../../types/poll';
import { GroupInterface } from '../../../types/group';

import PollMainButtonGroup from '../PollMainButtonGroup/PollMainButtonGroup';

function PollMainContainer() {
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const [polls, setPolls] = useState<getPollsResponse>([]);

  useEffect(() => {
    const fetchPolls = async () => {
      const res = await getPolls(groupCode);
      setPolls(res);
    };

    try {
      fetchPolls();
    } catch (err) {
      alert(err);
    }
  }, []);

  return (
    <StyledContainer>
      {polls ? (
        polls.map(({ status, title, id, code, isAnonymous, allowedPollCount }) => (
          <Box
            width="26.4rem"
            padding="2rem"
            minHeight="16.8rem"
            filter={status === 'CLOSED' ? 'grayscale(1)' : 'none'}
          >
            <FlexContainer justifyContent="end">
              <PollMainStatus status={status} />
            </FlexContainer>
            <StyledTitle>{title}</StyledTitle>
            <PollMainProgress pollCode={code} groupCode={groupCode} />
            <MarginContainer margin="0 0 1.2rem">
              {/* TODO: 'detail' 컴포넌트명 변경 고민(전체 페이지 수정 필요) */}
              <PollMainDetail isAnonymous={isAnonymous} allowedPollCount={allowedPollCount} />
            </MarginContainer>
            <PollMainButtonGroup pollCode={code} status={status} />
          </Box>
        ))
      ) : (
        <div>없습니다</div>
      )}
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  column-gap: 3.2rem;
  row-gap: 3.2rem;
`;

const StyledTitle = styled.h1`
  font-size: 1.6rem;
  text-align: center;
`;

export default PollMainContainer;
