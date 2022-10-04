import styled from '@emotion/styled';
import { useNavigate } from 'react-router-dom';
import Leave from '../../../../assets/leave.svg';
import { removeLocalStorageItem } from '../../../../utils/storage';

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
      <StyledLeaveImage src={Leave} alt="group-leave-button" />
      <StyledText>로그아웃</StyledText>
    </StyledContainer>
  );
}

const StyledContainer = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
`;

const StyledLeaveImage = styled.img`
  width: 2rem;
  filter: invert(65%) sepia(7%) saturate(12%) hue-rotate(1deg) brightness(92%) contrast(84%);
`;

const StyledText = styled.p(
  ({ theme }) => `
  font-size: 1.6rem; 
  color: ${theme.colors.GRAY_400}
`
);

export default SidebarLogoutMenu;
