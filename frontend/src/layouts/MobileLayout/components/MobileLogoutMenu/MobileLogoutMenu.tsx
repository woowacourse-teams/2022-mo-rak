import { useNavigate } from 'react-router-dom';
import Leave from '../../../../assets/leave.svg';
import { removeLocalStorageItem } from '../../../../utils/storage';
import { StyledContainer, StyledLeaveImage, StyledText } from './MobileLogoutMenu.styles';

function MobileLogoutMenu() {
  const navigate = useNavigate();

  const handleLogout = () => {
    if (confirm('로그아웃을 하시겠습니까?')) {
      removeLocalStorageItem('token');
      navigate('/');
    }
  };

  return (
    <StyledContainer onClick={handleLogout}>
      <StyledLeaveImage src={Leave} alt="group-leave-button" />
      <StyledText>로그아웃</StyledText>
    </StyledContainer>
  );
}

export default MobileLogoutMenu;

