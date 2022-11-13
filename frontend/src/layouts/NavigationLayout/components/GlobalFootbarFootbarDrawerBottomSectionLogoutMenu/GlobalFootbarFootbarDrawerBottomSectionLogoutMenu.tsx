import { useNavigate } from 'react-router-dom';
import leaveImg from '@/assets/leave.svg';
import { removeLocalStorageItem } from '@/utils/storage';
import {
  StyledContainer,
  StyledLeaveImage,
  StyledText
} from '@/layouts/NavigationLayout/components/GlobalFootbarFootbarDrawerBottomSectionLogoutMenu/GlobalFootbarFootbarDrawerBottomSectionLogoutMenu.styles';
import { CONFIRM_MESSAGE } from '@/constants/message';

function GlobalFootbarFootbarDrawerBottomSectionLogoutMenu() {
  const navigate = useNavigate();

  const handleLogout = () => {
    if (confirm(CONFIRM_MESSAGE.LOGOUT)) {
      removeLocalStorageItem('token');
      navigate('/');
    }
  };

  return (
    <StyledContainer onClick={handleLogout}>
      <StyledLeaveImage src={leaveImg} alt="group-leave-button" />
      <StyledText>로그아웃</StyledText>
    </StyledContainer>
  );
}

export default GlobalFootbarFootbarDrawerBottomSectionLogoutMenu;
