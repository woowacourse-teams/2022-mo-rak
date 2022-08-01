import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';

import { useTheme } from '@emotion/react';
import { useParams } from 'react-router-dom';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';
import Box from '../../components/common/Box/Box';
import Avatar from '../../components/common/Avatar/Avatar';
import Button from '../../components/common/Button/Button';
import { getAppointmentResult, getAppointment } from '../../api/appointment';
import { AppointmentResultInterface, AppointmentInfoInterface } from '../../types/appointment';
import { MemberInterface } from '../../types/group';
import { getGroupMembers } from '../../api/group';
import Crown from '../../assets/crown.svg';

function AppointmentResultPage() {
  const theme = useTheme();

  const [appointmentInfo, setAppointmentInfo] = useState<AppointmentInfoInterface>();
  const [appointmentResult, setAppointmentResult] = useState<Array<AppointmentResultInterface>>([]);
  const [clickedRank, setClickedRank] = useState(0);
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);

  const { groupCode, appointmentCode } = useParams() as {
    groupCode: string;
    appointmentCode: string
  };

  const totalParticipants = groupMembers.length;
  const week = ['일', '월', '화', '수', '목', '금', '토'];

  useEffect(() => {
    const fetchAppointment = async () => {
      const res = await getAppointment(groupCode, appointmentCode);
      setAppointmentInfo(res);
    };

    const fetchAppointmentResult = async () => {
      const res = await getAppointmentResult(groupCode, appointmentCode);
      setAppointmentResult(res);
    };

    try {
      fetchAppointment();
      fetchAppointmentResult();
    } catch (err) {
      alert(err);
    }
  }, []);

  useEffect(() => {
    const fetchGroupMembers = async () => {
      try {
        if (groupCode) {
          const res = await getGroupMembers(groupCode);

          setGroupMembers(res);
        }
      } catch (err) {
        if (err instanceof Error) {
          console.log(err);
        }
      }
    };
    fetchGroupMembers();
  }, [groupCode]);

  const handleShowParticipant = (rank: AppointmentResultInterface['rank']) => () => {
    setClickedRank(rank);
  };

  return (
    <StyledContainer>
      {appointmentInfo
        ? (
          <FlexContainer flexDirection="column" gap="4rem">
            <StyledTitle>{appointmentInfo.title}</StyledTitle>
            <FlexContainer gap="4rem">
              {/* TODO: scroll으로 인해 height가 필요해서, Box 컴포넌트를 쓰지 않고 styled component를 만듦 */}
              <StyledResultBox>
                {/* 몇일도 가능하게 했는지? */}
                {appointmentResult.map(({ rank, recommendStartDateTime, recommendEndDateTime, availableMembers }: AppointmentResultInterface) => (
                  <StyledRank onClick={handleShowParticipant(rank)} isClicked={rank === clickedRank}>
                    {/* 1등은 숫자 대신 왕관 */}
                    <FlexContainer justifyContent="space-between">
                      {rank === 1 ? (
                        <StyledCrownIcon src={Crown} alt="crown" />
                      ) : (
                        <StyledResultText>{rank}</StyledResultText>
                      )}
                      <StyledResultText>
                        {new Date(recommendStartDateTime).getFullYear()}
                        .
                        {new Date(recommendStartDateTime).getMonth() + 1}
                        .
                        {new Date(recommendStartDateTime).getDate()}
                        (
                        {week[new Date(recommendStartDateTime).getDay()]}
                        )
                        {new Date(recommendStartDateTime).getHours()}
                        :
                        {new Date(recommendStartDateTime).getMinutes()}
                        ~
                        {new Date(recommendEndDateTime).getFullYear()}
                        .
                        {new Date(recommendEndDateTime).getMonth() + 1}
                        .
                        {new Date(recommendEndDateTime).getDate()}
                        (
                        {week[new Date(recommendEndDateTime).getDay()]}
                        )
                        {new Date(recommendEndDateTime).getHours()}
                        :
                        {new Date(recommendEndDateTime).getMinutes()}
                      </StyledResultText>
                      <StyledResultText>
                        {availableMembers.length}
                        /
                        {totalParticipants}
                        명 가능
                      </StyledResultText>
                    </FlexContainer>
                  </StyledRank>
                ))}
              </StyledResultBox>

              <FlexContainer flexDirection="column" gap="3.8rem">
                <Box width="42rem" minHeight="28rem">
                  <StyledSmallTitle>가능한 사람</StyledSmallTitle>
                  <FlexContainer gap="0.8rem" justifyContent="center">
                    {clickedRank === 0
                      ? <StyledGuideText>왼쪽에서 보고싶은 결과를 클릭하세요!</StyledGuideText>
                      : appointmentResult[clickedRank - 1].availableMembers.map(({ name, profileUrl }) => (
                        <Avatar profileUrl={profileUrl} name={name} />
                      ))}
                  </FlexContainer>
                </Box>
                <Box width="42rem" minHeight="28rem">
                  <StyledSmallTitle>설득할 사람</StyledSmallTitle>
                  <FlexContainer gap="0.8rem" justifyContent="center">
                    {clickedRank === 0
                      ? <StyledGuideText>왼쪽에서 보고싶은 결과를 클릭하세요!</StyledGuideText>
                      : appointmentResult[clickedRank - 1].unavailableMembers.map(({ name, profileUrl }) => (
                        <Avatar profileUrl={profileUrl} name={name} />
                      ))}
                  </FlexContainer>
                </Box>
              </FlexContainer>
            </FlexContainer>
            <FlexContainer gap="4rem">
              <Button variant="filled" colorScheme={theme.colors.GRAY_300} width="30rem" padding="2.4rem" fontSize="4rem">마감</Button>
              <Button variant="filled" colorScheme={theme.colors.PURPLE_100} width="30rem" padding="2.4rem" fontSize="4rem">가능시간수정</Button>
            </FlexContainer>
          </FlexContainer>
        )
        : <div>로딩중</div>}
    </StyledContainer>
  );
}

const StyledTitle = styled.h1`
  font-size: 4rem;
`;

const StyledSmallTitle = styled.h1`
  font-size: 2rem;  
`;

const StyledContainer = styled.div`
  width: calc(100% - 36.4rem);
  display: flex;
  align-items: center;
  justify-content: center;
`;

const StyledResultBox = styled.div(({ theme }) => `
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
  width: 78rem; 
  height: 59.6rem;
  overflow-y: scroll;
  border-radius: 15px;
  background-color: ${theme.colors.WHITE_100};
  box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_25};
  padding: 2rem 0;
`);

const StyledResultText = styled.span`
  font-size: 2.4rem;
`;

const StyledCrownIcon = styled.img`
  width: 2rem;
`;

const StyledRank = styled.div<
  {
    isClicked: boolean;
  }
>(({ theme, isClicked }) => `
  border: 0.1rem solid ${theme.colors.TRANSPARENT_BLACK_25};
  background-color: ${isClicked ? theme.colors.PURPLE_100 : theme.colors.WHITE_100};
  color: ${isClicked ? theme.colors.WHITE_100 : theme.colors.BLACK_100};
  text-align: center;
  width: 66.8rem;
  border-radius: 0.8rem;
  padding: 2rem 4.4rem;
  box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_25};
  cursor: pointer;
`);

const StyledGuideText = styled.div(({ theme }) => `
  font-size: 2rem;
  color: ${theme.colors.GRAY_400};
`);

export default AppointmentResultPage;
