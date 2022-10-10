import { StyledTitle } from './MainFeatureMenuContainer.styles';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/appointment.svg';
import Role from '../../../../assets/role.svg';
import Divider from '../../../../components/Divider/Divider';
import MainFeatureMenu from '../MainFeatureMenu/MainFeatureMenu';

function MainFeatureMenuContainer() {
  const navigate = useNavigate();
  // TODO: util로 빼기
  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleAlert = (message: string) => () => {
    alert(message);
  };

  return (
    <FlexContainer flexDirection="column" gap="4rem">
      <StyledTitle>사용할 기능을 선택하세요!</StyledTitle>
      <Divider />

      <FlexContainer justifyContent="center" gap="8rem">
        <MainFeatureMenu onClick={handleNavigate('poll')} menu="투표하기" menuImg={Poll} />
        <MainFeatureMenu
          onClick={handleNavigate('appointment')}
          menu="약속잡기"
          menuImg={Appointment}
        />
        <MainFeatureMenu
          onClick={handleAlert('준비중인 서비스입니다.')}
          menu="역할 정하기"
          menuImg={Role}
        />
      </FlexContainer>
    </FlexContainer>
  );
}

export default MainFeatureMenuContainer;
