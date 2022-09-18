import styled from '@emotion/styled';

import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Box from '../../@common/Box/Box';
import { AppointmentRecommendationInterface } from '../../../types/appointment';
import Avatar from '../../@common/Avatar/Avatar';

interface Props {
  appointmentRecommendation: Array<AppointmentRecommendationInterface>;
  clickedRecommendation: number;
}

function AppointmentResultAvailableMembers({
  appointmentRecommendation,
  clickedRecommendation
}: Props) {
  return (
    <FlexContainer flexDirection="column" gap="3.6rem">
      <Box width="42rem" minHeight="28rem" padding="4rem" overflow="auto">
        <FlexContainer flexDirection="column" gap="4rem">
          <StyledSmallTitle>가능한 사람</StyledSmallTitle>
          <FlexContainer gap="0.8rem" justifyContent="flex-start">
            {clickedRecommendation === -1 ? (
              <StyledGuideText>왼쪽에서 보고싶은 결과를 클릭하세요!</StyledGuideText>
            ) : (
              appointmentRecommendation[clickedRecommendation].availableMembers.map(
                ({ name, profileUrl }) => (
                  <Avatar key={`${name}-${profileUrl}`} profileUrl={profileUrl} name={name} width="7rem" />
                )
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
                ({ name, profileUrl }) => (
                  <Avatar key={`${name}-${profileUrl}`} profileUrl={profileUrl} name={name} width="7rem" />
                )
              )
            )}
          </FlexContainer>
        </FlexContainer>
      </Box>
    </FlexContainer>
  );
}

const StyledGuideText = styled.div(
  ({ theme }) => `
  font-size: 2rem;
  color: ${theme.colors.GRAY_400};
`
);

const StyledSmallTitle = styled.h1`
  font-size: 2rem;
`;

export default AppointmentResultAvailableMembers;
