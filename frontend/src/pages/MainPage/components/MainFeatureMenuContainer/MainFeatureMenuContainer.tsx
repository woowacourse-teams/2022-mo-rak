import { useNavigate } from 'react-router-dom';

import MainFeatureMenu from '@/pages/MainPage/components/MainFeatureMenu/MainFeatureMenu';

import FlexContainer from '@/components/FlexContainer/FlexContainer';

import appointmentImg from '@/assets/appointment.svg';
import pollImg from '@/assets/poll.svg';
import roleImg from '@/assets/role.svg';
import { Menu } from '@/types/menu';

function MainFeatureMenuContainer() {
  const navigate = useNavigate();
  // TODO: util로 빼기
  const handleNavigate = (location: Menu) => () => {
    if (location === null) return;

    navigate(location);
  };

  return (
    <FlexContainer justifyContent="center" gap="8rem" flexWrap="wrap">
      <MainFeatureMenu onClick={handleNavigate('poll')} name="투표하기" img={pollImg} />
      <MainFeatureMenu
        onClick={handleNavigate('appointment')}
        name="약속잡기"
        img={appointmentImg}
      />
      <MainFeatureMenu onClick={handleNavigate('role')} name="역할 정하기" img={roleImg} />
    </FlexContainer>
  );
}

export default MainFeatureMenuContainer;
