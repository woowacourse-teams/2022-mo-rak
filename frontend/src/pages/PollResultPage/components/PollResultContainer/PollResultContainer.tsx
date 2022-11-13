import { useEffect, useState } from 'react';
import {
  StyledTitle,
  StyledLoadingContainer,
  StyledHelpIconContainer,
  StyledHelpIcon,
  StyledDescription
} from '@/pages/PollResultPage/components/PollResultContainer/PollResultContainer.styles';

import { useNavigate, useParams } from 'react-router-dom';
import Box from '@/components/Box/Box';
import Divider from '@/components/Divider/Divider';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import MarginContainer from '@/components/MarginContainer/MarginContainer';

import PollResultItems from '@/pages/PollResultPage/components/PollResultItems/PollResultItems';
import PollResultDetail from '@/pages/PollResultPage/components/PollResultDetail/PollResultDetail';
import PollResultButtons from '@/pages/PollResultPage/components/PollResultButtons/PollResultButtons';
import { getPoll, getPollResult, getPollItems } from '@/api/poll';
import { Poll, getPollResponse, getPollResultResponse, getPollItemsResponse } from '@/types/poll';
import { Group } from '@/types/group';

import PollResultProgress from '@/pages/PollResultPage/components/PollResultProgress/PollResultProgress';
import PollResultStatus from '@/pages/PollResultPage/components/PollResultStatus/PollResultStatus';
import PollResultShareLink from '@/pages/PollResultPage/components/PollResultShareLink/PollResultShareLink';
import { AxiosError } from 'axios';
import Spinner from '@/components/Spinner/Spinner';
import Tooltip from '@/components/Tooltip/Tooltip';
import questionImg from '@/assets/question.svg';
import { useTheme } from '@emotion/react';
import { POLL_ERROR } from '@/constants/errorMessage';

function PollResultContainer() {
  const navigate = useNavigate();
  const { groupCode, pollCode } = useParams() as {
    groupCode: Group['code'];
    pollCode: Poll['code'];
  };
  const [poll, setPoll] = useState<getPollResponse>();
  const [pollResult, setPollResult] = useState<getPollResultResponse>([]);
  const [pollItems, setPollItems] = useState<getPollItemsResponse>([]);
  const theme = useTheme();

  const setStatus = (status: Poll['status']) => {
    // TODO: if appointment가 맞나?...없을 수도 있어서 undefined error가 발생
    if (poll) {
      setPoll({ ...poll, status });
    }
  };

  useEffect(() => {
    (async () => {
      try {
        const res = await getPoll(pollCode, groupCode);
        setPoll(res.data);
      } catch (err) {
        if (err instanceof AxiosError) {
          const errCode = err.response?.data.codeNumber;

          if (errCode === '2300') {
            alert(POLL_ERROR.NOT_EXIST);
            navigate(`/groups/${groupCode}/poll`);
          }
        }
      }
    })();
  }, []);

  useEffect(() => {
    (async () => {
      const res = await getPollResult(pollCode, groupCode);
      setPollResult(res.data);
    })();
  }, []);

  useEffect(() => {
    (async (pollCode: Poll['code']) => {
      const res = await getPollItems(pollCode, groupCode);
      setPollItems(res.data);
    })(pollCode);
  }, []);

  return (
    <Box width="84.4rem" minHeight="60rem" padding="6.4rem 4.8rem">
      {poll ? (
        <>
          <FlexContainer justifyContent="end">
            <MarginContainer margin="0 0 1.4rem 0">
              <FlexContainer gap="1.2rem" alignItems="center" justifyContent="flex-end">
                <PollResultShareLink
                  groupCode={groupCode}
                  pollCode={pollCode}
                  status={poll.status}
                />
                <PollResultStatus status={poll.status} />
              </FlexContainer>
              <MarginContainer margin="1.2rem 0 0">
                <Tooltip
                  content="사람 아이콘을 클릭해보세요. 항목별로 투표 한 사람들과 이유를 볼 수 있어요 👀"
                  width="32rem"
                  placement="bottom"
                  backgroundColor={theme.colors.PURPLE_50}
                  fontSize="1.6rem"
                >
                  <FlexContainer gap="1.2rem" alignItems="center">
                    <StyledHelpIconContainer>
                      <StyledHelpIcon src={questionImg} alt="help-icon" />
                    </StyledHelpIconContainer>
                    <StyledDescription>투표 한 사람 확인</StyledDescription>
                  </FlexContainer>
                </Tooltip>
              </MarginContainer>
            </MarginContainer>
          </FlexContainer>
          <MarginContainer margin="0 0 1.4rem 0">
            <StyledTitle>{poll.title}</StyledTitle>
            <Divider />
          </MarginContainer>
          <MarginContainer margin="1.4rem 0">
            <PollResultProgress currentParticipants={poll.count} />
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
              <PollResultItems status={poll.status} pollResult={pollResult} pollItems={pollItems} />
            </FlexContainer>
          </MarginContainer>
          <PollResultButtons
            isHost={poll.isHost}
            status={poll.status}
            pollCode={poll.code}
            groupCode={groupCode}
            pollItems={pollItems}
            setStatus={setStatus}
          />
        </>
      ) : (
        <StyledLoadingContainer>
          <Spinner width="15%" placement="center" />
        </StyledLoadingContainer>
      )}
    </Box>
  );
}

export default PollResultContainer;
