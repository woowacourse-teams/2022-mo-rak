import {
  StlyedImageWrapper,
  StyledFeatureMenuContainer,
  StyledFeatureMenuName,
  StyledImage,
  StyledTitle
} from './MainFeatureMenuContainer.styles';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/appointment.svg';

function MainFeatureMenuContainer() {
  const navigate = useNavigate();
  // TODO: util로 빼기
  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <FlexContainer flexDirection="column">
      <StyledTitle>사용할 기능을 선택하세요!</StyledTitle>

      <FlexContainer justifyContent="center" gap="8rem">
        {/* TODO: 네이밍 고민 */}
        <StyledFeatureMenuContainer onClick={handleNavigate('poll')}>
          <StlyedImageWrapper>
            {/* TODO: StyledImage 네이밍 고민 */}
            <StyledImage src={Poll} alt="poll-menu" />
          </StlyedImageWrapper>
          <StyledFeatureMenuName>투표하기</StyledFeatureMenuName>
        </StyledFeatureMenuContainer>
        <StyledFeatureMenuContainer onClick={handleNavigate('appointment')}>
          <StlyedImageWrapper>
            <StyledImage src={Appointment} alt="appointment-menu" />
          </StlyedImageWrapper>
          <StyledFeatureMenuName>약속잡기</StyledFeatureMenuName>
        </StyledFeatureMenuContainer>
      </FlexContainer>
    </FlexContainer>
  );
}

export default MainFeatureMenuContainer;
