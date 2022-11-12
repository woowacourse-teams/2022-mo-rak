import { useNavigate } from 'react-router-dom';

import {
  StyledContainer,
  StyledLeaveImage,
  StyledText
} from '@/layouts/NavigationLayout/components/SidebarLogoutMenu/SidebarLogoutMenu.styles';

import leaveImg from '@/assets/leave.svg';
import { removeLocalStorageItem } from '@/utils/storage';

function SidebarLogoutMenu() {
  const navigate = useNavigate();

  const handleLogout = () => {
    if (confirm('로그아웃을 하시겠습니까?')) {
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

export default SidebarLogoutMenu;
