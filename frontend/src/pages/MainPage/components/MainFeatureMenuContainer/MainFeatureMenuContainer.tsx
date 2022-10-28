import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/appointment.svg';
import Role from '../../../../assets/role.svg';
import MainFeatureMenu from '../MainFeatureMenu/MainFeatureMenu';
import { Menu } from '../../../../types/menu';

function MainFeatureMenuContainer() {
  const navigate = useNavigate();
  // TODO: util로 빼기
  const handleNavigate = (location: Menu) => () => {
    if (location === null) return;

    navigate(location);
  };

  return (
    <FlexContainer justifyContent="center" gap="8rem" flexWrap="wrap">
      <MainFeatureMenu onClick={handleNavigate('poll')} name="투표하기" img={Poll} />
      <MainFeatureMenu onClick={handleNavigate('appointment')} name="약속잡기" img={Appointment} />
      <MainFeatureMenu onClick={handleNavigate('role')} name="역할 정하기" img={Role} />
    </FlexContainer>
  );
}

export default MainFeatureMenuContainer;
