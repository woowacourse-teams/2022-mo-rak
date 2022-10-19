import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import Box from '../../../../components/Box/Box';
import { AppointmentRecommendation } from '../../../../types/appointment';
import Avatar from '../../../../components/Avatar/Avatar';
import { StyledGuideText, StyledSmallTitle } from './AppointmentResultAvailableMembers.styles';

type Props = {
  appointmentRecommendation: Array<AppointmentRecommendation>;
  clickedRecommendation: number;
};

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
                  <Avatar
                    key={`${name}-${profileUrl}`}
                    profileUrl={profileUrl}
                    name={name}
                    width="7rem"
                  />
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
                  <Avatar
                    key={`${name}-${profileUrl}`}
                    profileUrl={profileUrl}
                    name={name}
                    width="7rem"
                  />
                )
              )
            )}
          </FlexContainer>
        </FlexContainer>
      </Box>
    </FlexContainer>
  );
}

export default AppointmentResultAvailableMembers;
