import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import Box from '../../components/common/Box/Box';
import Button from '../../components/common/Button/Button';
import FlexContainer from '../../components/common/FlexContainer/FlexContainer';

function AppointmentProgressPage() {
  console.log('?');

  const theme = useTheme();
  // NOTE: 이렇게 Left,Right가 있을 때는 어떻게 추상화 레벨을 맞춰줄까?...
  return (
    <StyledContainer>
      <StyledLeftContainer>
        {/* header-start */}
        <StyledHeaderContainer>
          <StyledHeader>2주차 스터디</StyledHeader>
          <StyledDescription>
            여기는 description입니다. 알아서 추상화 레벨 잘 나눠주세요~ 알겠죠? 여기는
            description입니다. 알아서 추상화 레벨 잘 나눠주세요~ 알겠죠? 여기는 description입니다.
            알아서 추상화 레벨 잘 나눠주세요~ 알겠죠? 여기는 description입니다. 알아서 추상화 레벨
            잘 나눠주세요~ 알겠죠? 여기는 description입니다. 알아서 추상화 레벨 잘 나눠주세요~
            알겠죠?
          </StyledDescription>
        </StyledHeaderContainer>
        {/* header-end */}
        <Box width="45.2rem" minHeight="60rem" />
      </StyledLeftContainer>
      <StyledRightContainer>
        {/* detail-start */}
        <StyledDuration>2시간 00분동안 진행</StyledDuration>
        <StyledTimeRange>AM10:00 ~ AM12:00</StyledTimeRange>
        {/* detail-end */}
        {/* timePicker-start */}
        <Box width="30rem" height="58rem" padding="3.6rem 2rem" overflow="auto">
          <FlexContainer flexDirection="column" gap="1.2rem">
            <StyledTime
              onMouseDown={(e) => console.log(e.target, 'down')}
              onMouseUp={(e) => console.log(e.target, 'up')}
              onMouseEnter={(e) => console.log(e.target, 'entered')}
            >
              1
            </StyledTime>
            <StyledTime
              onMouseDown={(e) => console.log(e.target, 'down')}
              onMouseUp={(e) => console.log(e.target, 'up')}
              onMouseEnter={(e) => console.log(e.target, 'entered')}
            >
              2
            </StyledTime>
            <StyledTime
              onMouseDown={(e) => console.log(e.target, 'down')}
              onMouseUp={(e) => console.log(e.target, 'up')}
              onMouseEnter={(e) => console.log(e.target, 'entered')}
            >
              3
            </StyledTime>
          </FlexContainer>
        </Box>
        {/* timePicker-end */}
        {/* submitButton-start */}
        <Button
          type="submit"
          variant="filled"
          colorScheme={theme.colors.PURPLE_100}
          width="31.6rem"
          fontSize="4rem"
        >
          선택
        </Button>
        {/* submitButton-end */}
      </StyledRightContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  display: flex;
  width: calc(100% - 36.4rem);
  align-items: center;
  justify-content: center;
  gap: 18.4rem;
`;

const StyledLeftContainer = styled.div`
  display: flex;
  width: 45.2rem;
  flex-direction: column;
  gap: 1.8rem;
`;

const StyledHeaderContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 3.6rem;
  max-height: 20.8rem;
  min-height: 20.8rem;
`;

const StyledHeader = styled.header`
  font-size: 4.8rem;
`;

const StyledDescription = styled.div`
  font-size: 2rem;
  overflow-y: scroll;
`;

const StyledRightContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 38.8rem;
  gap: 1.2rem;
  align-items: center;
`;

const StyledDuration = styled.p`
  font-size: 4rem;
`;

const StyledTimeRange = styled.p`
  font-size: 3.2rem;
`;

const StyledTime = styled.div(
  ({ theme }) => `
  width: 25.6rem;
  font-size: 1.6rem;
  background-color: ${theme.colors.TRANSPARENT_YELLOW_100_33};
  text-align: center;
  padding: 0.8rem 0;
  border-radius: 5px;
  cursor: pointer;

  :hover {
    background-color: ${theme.colors.YELLOW_100};
  }
`
);

export default AppointmentProgressPage;
