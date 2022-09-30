import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';

import Poll from '../../../assets/poll.svg';
import Appointment from '../../../assets/appointment.svg';

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

const StyledFeatureMenuContainer = styled.div`
  cursor: pointer;
`;

const StyledTitle = styled.h1`
  display: block;
  font-size: 4.8rem;
  text-align: center;
  margin: 0 0 8rem;
`;

const StlyedImageWrapper = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  font-size: 3.2rem;
  border-radius: 100%;
  background: ${theme.colors.GRAY_200};
  padding: 8rem;  

  &:hover {
    background: ${theme.colors.GRAY_300};
  }
  `
);

const StyledImage = styled.img`
  width: 24rem;
  aspect-ratio: 1 / 1;
`;

const StyledFeatureMenuName = styled.div`
  margin: 2rem 0 0;
  text-align: center;
  font-size: 4rem;
`;

export default MainFeatureMenuContainer;
