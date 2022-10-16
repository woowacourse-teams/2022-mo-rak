import { StyledContainer, StyledLogo } from './MobileHeader.styles';
import Logo from '../../../../assets/logo.svg';
import { useNavigate } from 'react-router-dom';
import { GroupInterface } from '../../../../types/group';

interface Props {
  groupCode: GroupInterface['code'];
}

function MobileHeader({ groupCode }: Props) {
  const navigate = useNavigate();

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
