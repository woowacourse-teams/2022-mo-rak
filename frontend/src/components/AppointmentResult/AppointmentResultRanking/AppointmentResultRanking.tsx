import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';

import Box from '../../common/Box/Box';
import Avatar from '../../common/Avatar/Avatar';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import Crown from '../../../assets/crown.svg';
import { getAppointmentRecommendation } from '../../../api/appointment';
import {
  AppointmentInterface,
  AppointmentRecommendationInterface
} from '../../../types/appointment';
import { MemberInterface, GroupInterface } from '../../../types/group';
import { getGroupMembers } from '../../../api/group';

interface Props {
  groupCode: GroupInterface['code'];
  appointmentCode: AppointmentInterface['code'];
}

const getDateTime = (
  // TODO: 타이핑 고민해보기
  recommendDateTime: AppointmentRecommendationInterface[
    | 'recommendStartDateTime'
    | 'recommendEndDateTime']
) => {
  // TODO: 리팩토링
  // TODO: recommend? recommendation? 변수명 고민해보기
  const period = recommendDateTime.slice(-2);
  const dateTime = new Date(recommendDateTime.slice(0, -2));
  const week = ['일', '월', '화', '수', '목', '금', '토'];

  const year = dateTime.getFullYear();
  const month = dateTime.getMonth() + 1;
  const date = dateTime.getDate();
  const day = week[dateTime.getDay()];
  const hour = dateTime.getHours().toString().padStart(2, '0');
  const minutes = dateTime.getMinutes().toString().padStart(2, '0');

  return ` ${year}.${month}.${date}(${day}) ${hour}:${minutes}${period} `;
};

function AppointmentResultRanking({ groupCode, appointmentCode }: Props) {
  const [appointmentRecommendation, setAppointmentRecommendation] = useState<
    Array<AppointmentRecommendationInterface>
  >([]);
  // TODO: -1에 대해서 생각해보기
  const [clickedRecommendation, setClickedRecommendation] = useState<number>(-1);
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);

  const totalParticipants = groupMembers.length;

  useEffect(() => {
    const fetchAppointmentRecommendation = async () => {
      try {
        const res = await getAppointmentRecommendation(groupCode, appointmentCode);
        setAppointmentRecommendation(res.data);
      } catch (err) {
        alert(err);
      }
    };
    fetchAppointmentRecommendation();
  }, []);

  useEffect(() => {
    const fetchGroupMembers = async () => {
      try {
        if (groupCode) {
          const res = await getGroupMembers(groupCode);
          setGroupMembers(res.data);
        }
      } catch (err) {
        if (err instanceof Error) {
          console.log(err);
        }
      }
    };
    fetchGroupMembers();
  }, [groupCode]);

  const handleShowParticipant = (idx: number) => () => {
    setClickedRecommendation(idx);
  };

  return (
    <FlexContainer gap="4rem">
      {/* TODO: scroll으로 인해(overflow-y) height가 필요해서, Box 컴포넌트를 쓰지 않고 styled component를 만듦 */}
      <StyledResultBox>
        {appointmentRecommendation.map(
          (
            {
              rank,
              recommendStartDateTime,
              recommendEndDateTime,
              availableMembers
            }: AppointmentRecommendationInterface,
            idx
          ) => (
            <StyledRank
              onClick={handleShowParticipant(idx)}
              isClicked={idx === clickedRecommendation}
            >
              <FlexContainer justifyContent="space-between">
                {/* TODO: 상수화 */}
                {rank === 1 ? (
                  <StyledCrownIcon src={Crown} alt="crown" />
                ) : (
                  // TODO: Text라는 suffix에 대해서 일관성 살펴보기
                  <StyledResultText>{rank}</StyledResultText>
                )}
                <StyledResultText>
                  {getDateTime(recommendStartDateTime)}~{getDateTime(recommendEndDateTime)}
                </StyledResultText>
                <StyledResultText>
                  {availableMembers.length}/{totalParticipants}명 가능
                </StyledResultText>
              </FlexContainer>
            </StyledRank>
          )
        )}
      </StyledResultBox>

      <FlexContainer flexDirection="column" gap="3.6rem">
        <Box width="42rem" minHeight="28rem" padding="4rem" overflow="auto">
          <FlexContainer flexDirection="column" gap="4rem">
            <StyledSmallTitle>가능한 사람</StyledSmallTitle>
            <FlexContainer gap="0.8rem" justifyContent="flex-start">
              {clickedRecommendation === -1 ? (
                <StyledGuideText>왼쪽에서 보고싶은 결과를 클릭하세요!</StyledGuideText>
              ) : (
                appointmentRecommendation[clickedRecommendation].availableMembers.map(
                  ({ name, profileUrl }) => <Avatar profileUrl={profileUrl} name={name} />
                )
              )}
            </FlexContainer>
          </FlexContainer>
        </Box>
        <Box width="42rem" minHeight="28rem" padding="4rem" overflow="auto">
          <FlexContainer flexDirection="column" gap="4rem">
            <StyledSmallTitle>설득할 사람</StyledSmallTitle>
            <FlexContainer gap="0.8rem" justifyContent="flex-start">
              {clickedRecommendation === -1 ? (
                <StyledGuideText>왼쪽에서 보고싶은 결과를 클릭하세요!</StyledGuideText>
              ) : (
                appointmentRecommendation[clickedRecommendation].unavailableMembers.map(
                  ({ name, profileUrl }) => <Avatar profileUrl={profileUrl} name={name} />
                )
              )}
            </FlexContainer>
          </FlexContainer>
        </Box>
      </FlexContainer>
    </FlexContainer>
  );
}

const StyledResultBox = styled.div(
  ({ theme }) => `
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
  width: 78rem; 
  height: 59.6rem;
  overflow-y: auto;
  border-radius: 15px;
  background-color: ${theme.colors.WHITE_100};
  box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_100_25};
  padding: 2rem 0;
`
);

const StyledResultText = styled.span`
  font-size: 2rem;
`;

const StyledCrownIcon = styled.img`
  width: 2rem;
`;

const StyledRank = styled.div<{
  isClicked: boolean;
}>(
  // NOTE: 아래처럼 변수 사용하는 것은 밑으로 빼줘도 괜찮을듯?
  ({ theme, isClicked }) => `
  text-align: center;
  width: 66.8rem;
  border-radius: 0.8rem;
  padding: 2rem 4.4rem;
  cursor: pointer;
  
  border: 0.1rem solid ${theme.colors.TRANSPARENT_BLACK_100_25};
  box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_100_25};
  background-color: ${isClicked ? theme.colors.PURPLE_100 : theme.colors.WHITE_100};
  color: ${isClicked ? theme.colors.WHITE_100 : theme.colors.BLACK_100};
`
);

const StyledGuideText = styled.div(
  ({ theme }) => `
  font-size: 2rem;
  color: ${theme.colors.GRAY_400};
`
);

const StyledSmallTitle = styled.h1`
  font-size: 2rem;
`;

export default AppointmentResultRanking;
