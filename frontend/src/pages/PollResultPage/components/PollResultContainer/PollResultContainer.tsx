import { useEffect, useState } from 'react';
import styled from '@emotion/styled';

import { useParams } from 'react-router-dom';
import Box from '../../../../components/Box/Box';
import Divider from '../../../../components/Divider/Divider';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import MarginContainer from '../../../../components/MarginContainer/MarginContainer';

import PollResultItemGroup from '../PollResultItemGroup/PollResultItemGroup';
import PollResultDetail from '../PollResultDetail/PollResultDetail';
import PollResultButtonGroup from '../PollResultButtonGroup/PollResultButtonGroup';
import { getPoll, getPollResult, getPollItems } from '../../../../api/poll';
import {
  PollInterface,
  getPollResponse,
  getPollResultResponse,
  getPollItemsResponse
} from '../../../../types/poll';
import { GroupInterface } from '../../../../types/group';

import PollResultProgress from '../PollResultProgress/PollResultProgress';
import PollResultStatus from '../PollResultStatus/PollResultStatus';
import PollResultShareLink from '../PollResultShareLink/PollResultShareLink';

function PollResultContainer() {
  const { groupCode, pollCode } = useParams() as {
    groupCode: GroupInterface['code'];
    pollCode: PollInterface['code'];
  };
  const [poll, setPoll] = useState<getPollResponse>();
  const [pollResult, setPollResult] = useState<getPollResultResponse>([]);
  const [pollItems, setPollItems] = useState<getPollItemsResponse>([]);

  useEffect(() => {
    const fetchPoll = async () => {
      const res = await getPoll(pollCode, groupCode);
      setPoll(res.data);
    };

    const fetchPollResult = async () => {
      const res = await getPollResult(pollCode, groupCode);
      setPollResult(res.data);
    };

    try {
      fetchPoll();
      fetchPollResult();
    } catch (err) {
      alert(err);
    }
  }, []);

  useEffect(() => {
    const fetchPollItems = async (pollCode: PollInterface['code']) => {
      try {
        const res = await getPollItems(pollCode, groupCode);
        setPollItems(res.data);
      } catch (err) {
        alert(err);
      }
    };

    fetchPollItems(pollCode);
  }, []);

  return (
    <Box width="84.4rem" padding="6.4rem 4.8rem">
      {poll ? (
        <>
          <FlexContainer justifyContent="end">
            <MarginContainer margin="0 0 1.4rem 0">
              <FlexContainer gap="1.2rem" alignItems="center">
                <PollResultShareLink
                  groupCode={groupCode}
                  pollCode={pollCode}
                  status={poll.status}
                />
                <PollResultStatus status={poll.status} />
              </FlexContainer>
            </MarginContainer>
          </FlexContainer>
          <MarginContainer margin="0 0 1.4rem 0">
            <StyledTitle>{poll.title}</StyledTitle>
            <Divider />
          </MarginContainer>
          <MarginContainer margin="1.4rem 0">
            <PollResultProgress currentParticipants={poll.count} groupCode={groupCode} />
          </MarginContainer>
          <MarginContainer margin="1.4rem 0">
            <PollResultDetail
              isAnonymous={poll.isAnonymous}
              allowedPollCount={poll.allowedPollCount}
              closedAt={poll.closedAt}
            />
          </MarginContainer>
          <MarginContainer margin="0 0 15.2rem 0">
            <FlexContainer flexDirection="column" gap="1.2rem">
              <PollResultItemGroup
                status={poll.status}
                pollResult={pollResult}
                pollItems={pollItems}
              />
            </FlexContainer>
          </MarginContainer>
          <PollResultButtonGroup
            isHost={poll.isHost}
            status={poll.status}
            pollCode={poll.code}
            groupCode={groupCode}
            pollItems={pollItems}
          />
        </>
      ) : (
        <div>로딩중</div>
      )}
    </Box>
  );
}

const StyledTitle = styled.h1(
  ({ theme }) => `
  color: ${theme.colors.BLACK_100};
  font-size: 3.2rem;
`
);

export default PollResultContainer;
