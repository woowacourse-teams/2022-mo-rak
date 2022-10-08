import {
  StyledImageWrapper,
  StyledFeatureMenuContainer,
  StyledFeatureMenuName,
  StyledImage,
  StyledTitle
} from './MainFeatureMenuContainer.styles';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/appointment.svg';
import Role from '../../../../assets/role.svg';
import Divider from '../../../../components/Divider/Divider';

function MainFeatureMenuContainer() {
  const navigate = useNavigate();
  // TODO: util로 빼기
  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <FlexContainer flexDirection="column" gap="4rem">
      <StyledTitle>사용할 기능을 선택하세요!</StyledTitle>
      <Divider />

      <FlexContainer justifyContent="center" gap="8rem">
        {/* TODO: 네이밍 고민 */}
        <StyledFeatureMenuContainer onClick={handleNavigate('poll')}>
          <StyledImageWrapper>
            {/* TODO: StyledImage 네이밍 고민 */}
            <StyledImage src={Poll} alt="poll-menu" />
            <StyledFeatureMenuName>투표하기</StyledFeatureMenuName>
          </StyledImageWrapper>
        </StyledFeatureMenuContainer>

        <StyledFeatureMenuContainer onClick={handleNavigate('appointment')}>
          <StyledImageWrapper>
            <StyledImage src={Appointment} alt="appointment-menu" />
            <StyledFeatureMenuName>약속잡기</StyledFeatureMenuName>
          </StyledImageWrapper>
        </StyledFeatureMenuContainer>

        <StyledFeatureMenuContainer onClick={() => alert('준비중입니다.')}>
          <StyledImageWrapper>
            <StyledImage src={Role} alt="role-menu" />
            <StyledFeatureMenuName>역할 정하기</StyledFeatureMenuName>
          </StyledImageWrapper>
        </StyledFeatureMenuContainer>
      </FlexContainer>
    </FlexContainer>
  );
}

export default MainFeatureMenuContainer;
