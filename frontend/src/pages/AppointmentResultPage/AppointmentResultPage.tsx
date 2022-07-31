import React, { useEffect } from 'react';
import styled from '@emotion/styled';

import { useTheme } from '@emotion/react';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';
import Box from '../../components/common/Box/Box';
import TextField from '../../components/common/TextField/TextField';
import Avatar from '../../components/common/Avatar/Avatar';
import Button from '../../components/common/Button/Button';
import { getAppointmentResult } from '../../api/appointment';

function AppointmentResultPage() {
  const theme = useTheme();
  // const [appointmentResult, setAppointmentResult] = useState([]);

  // fetch해와서, fetch 데이터를 넣어줘야함
  const temp = [
    {
      rank: 1,
      recommendStartDateTime: '2022-07-30T10:00:00',
      recommendEndDateTime: '2022-08-01T01:00:00',
      availableMembers: [
        {
          id: 1,
          name: '리차리',
          profileUrl: 'www.naver.com'
        },
        {
          id: 2,
          name: '리차리',
          profileUrl: 'www.naver.com'
        }
      ],
      unavailableMembers: [
        {
          id: 3,
          name: '리차리',
          profileUrl: 'www.naver.com'
        },
        {
          id: 4,
          name: '리차리',
          profileUrl: 'www.naver.com'
        }
      ]
    }
  ];

  // useEffect(() => {
  //   const fetchAppointmentResult = async () => {
  //     const res = await getAppointmentResult(groupCode, appointmentCode);
  //     setAppointmentResult(res);
  //   };
  // }, []);

  return (
    <StyledContainer>
      <FlexContainer flexDirection="column" gap="4rem">
        <StyledTitle>2주차 스터디</StyledTitle>
        <FlexContainer gap="4rem">
          {/* TODO: scroll으로 인해 height가 필요해서, Box 컴포넌트를 쓰지 않고 styled component를 만듦 */}
          <StyledResultBox>
            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>

            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>

            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>

            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>

            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>

            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>

            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>

            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>

            <TextField variant="outlined" colorScheme={theme.colors.TRANSPARENT_BLACK_25} width="66.8rem" padding="2rem 4.4rem" boxShadow={theme.colors.TRANSPARENT_BLACK_25} borderRadius="0.8rem">
              {/* 1등은 숫자 대신 왕관 */}
              <FlexContainer justifyContent="space-between">
                <StyledResultText>2</StyledResultText>
                <StyledResultText>2022.08.11(화)</StyledResultText>
                <StyledResultText>14:00 ~ 16:00</StyledResultText>
                <StyledResultText>4/5명 가능</StyledResultText>
              </FlexContainer>
            </TextField>
          </StyledResultBox>

          <FlexContainer flexDirection="column" gap="3.8rem">
            <Box width="42rem" minHeight="28rem">
              <StyledSmallTitle>가능한 사람</StyledSmallTitle>
              <FlexContainer gap="0.8rem">
                <Avatar profileUrl="https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2" name="위니" />
                <Avatar profileUrl="https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2" name="위니" />
                <Avatar profileUrl="https://mblogthumb-phinf.pstatic.net/20160613_163/eogk8709_1465779890048M4obs_JPEG/43.jpg?type=w2" name="위니" />
              </FlexContainer>
            </Box>
            <Box width="42rem" minHeight="28rem">
              <StyledSmallTitle>설득할 사람</StyledSmallTitle>
            </Box>
          </FlexContainer>
        </FlexContainer>
        <FlexContainer gap="4rem">
          <Button variant="filled" colorScheme={theme.colors.GRAY_300} width="30rem" padding="2.4rem" fontSize="4rem">마감</Button>
          <Button variant="filled" colorScheme={theme.colors.PURPLE_100} width="30rem" padding="2.4rem" fontSize="4rem">가능시간수정</Button>
        </FlexContainer>
      </FlexContainer>
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

export default AppointmentResultPage;
