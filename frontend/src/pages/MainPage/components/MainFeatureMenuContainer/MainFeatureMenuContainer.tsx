import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/appointment.svg';
import Role from '../../../../assets/role.svg';
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
    <FlexContainer justifyContent="center" gap="8rem">
      <MainFeatureMenu onClick={handleNavigate('poll')} name="투표하기" img={Poll} />
      <MainFeatureMenu onClick={handleNavigate('appointment')} name="약속잡기" img={Appointment} />
      <MainFeatureMenu
        onClick={handleAlert('준비중인 서비스입니다.')}
        name="역할 정하기"
        img={Role}
      />
    </FlexContainer>
  );
}

export default MainFeatureMenuContainer;
