import { StyledContainer, StyledLogo } from './MobileHeader.styles';
import Logo from '../../../../assets/logo.svg';
import { useNavigate, useParams } from 'react-router-dom';
import { GroupInterface } from '../../../../types/group';
function MobileHeader() {
  const navigate = useNavigate();
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <StyledContainer>
      <StyledLogo src={Logo} alt={Logo} onClick={handleNavigate(`/groups/${groupCode}`)} />
    </StyledContainer>
  );
}

export default MobileHeader;
