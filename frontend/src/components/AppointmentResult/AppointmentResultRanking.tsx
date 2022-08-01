import React, { useState, useEffect } from 'react';
import styled from '@emotion/styled';

import Box from '../common/Box/Box';
import Avatar from '../common/Avatar/Avatar';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import Crown from '../../assets/crown.svg';
import { getAppointmentResult } from '../../api/appointment';
import { AppointmentResultInterface, AppointmentInterface } from '../../types/appointment';
import { MemberInterface, GroupInterface } from '../../types/group';
import { getGroupMembers } from '../../api/group';

interface Props {
  groupCode: GroupInterface['code'];
  appointmentCode: AppointmentInterface['code'];
}

function AppointmentResultRanking({ groupCode, appointmentCode }: Props) {
  // TODO: api 연결 전 화면 확인을 위해 초기값 임시 설정
  const [appointmentResult, setAppointmentResult] = useState<Array<AppointmentResultInterface>>([
    {
      rank: 1,
      recommendStartDateTime: '2022-07-30T10:00:00',
      recommendEndDateTime: '2022-08-01T01:00:00',
      availableMembers: [
        {
          id: 1,
          name: '위니',
          profileUrl: 'https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2'
        },
        {
          id: 2,
          name: '앨버',
          profileUrl: 'https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2'
        }
      ],
      unavailableMembers: [
        {
          id: 3,
          name: '엘리',
          profileUrl: 'https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2'
        },
        {
          id: 4,
          name: '에덴',
          profileUrl: 'https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2'
        }
      ]
    },
    {
      rank: 2,
      recommendStartDateTime: '2022-07-30T10:00:00',
      recommendEndDateTime: '2022-08-01T01:00:00',
      availableMembers: [
        {
          id: 1,
          name: '위니',
          profileUrl: 'https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2'
        },
        {
          id: 4,
          name: '에덴',
          profileUrl: 'https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2'
        }
      ],
      unavailableMembers: [
        {
          id: 2,
          name: '앨버',
          profileUrl: 'https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2'
        }
      ]
    }
  ]);
  const [clickedRank, setClickedRank] = useState(0);
  const [groupMembers, setGroupMembers] = useState<Array<MemberInterface>>([]);

  const totalParticipants = groupMembers.length;
  const week = ['일', '월', '화', '수', '목', '금', '토'];

  useEffect(() => {
    const fetchAppointmentResult = async () => {
      const res = await getAppointmentResult(groupCode, appointmentCode);
      setAppointmentResult(res);
    };

    try {
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

  const getDateTime = (recommendDateTime: AppointmentResultInterface['recommendStartDateTime' | 'recommendEndDateTime']) => {
    const dateTime = new Date(recommendDateTime);
    const year = dateTime.getFullYear();
    const month = dateTime.getMonth() + 1;
    const date = dateTime.getDate();
    const day = week[dateTime.getDay()];
    const hour = dateTime.getHours().toString().padStart(2, '0');
    const minutes = dateTime.getMinutes().toString().padStart(2, '0');

    return ` ${year}.${month}.${date}(${day}) ${hour}:${minutes} `;
  };

  return (
    <FlexContainer gap="4rem">
      {/* TODO: scroll으로 인해(overflow-y) height가 필요해서, Box 컴포넌트를 쓰지 않고 styled component를 만듦 */}
      <StyledResultBox>
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
                {getDateTime(recommendStartDateTime)}
                ~
                {getDateTime(recommendEndDateTime)}
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
  );
}

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

const StyledSmallTitle = styled.h1`
  font-size: 2rem;  
`;

export default AppointmentResultRanking;
