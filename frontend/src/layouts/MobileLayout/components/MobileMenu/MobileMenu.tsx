import { StyledContainer, StyledMenuIcon } from './MobileMenu.styles';
import Home from '../../../../assets/home.svg';
import Menu from '../../../../assets/menu.svg';
import Poll from '../../../../assets/poll.svg';
import Appointment from '../../../../assets/calendar-clock.svg';
import Role from '../../../../assets/role.svg';

function MobileMenu() {
  return (
    <StyledContainer>
      <button>
        <StyledMenuIcon src={Home} alt="main-page" />
      </button>
      <button>
        <StyledMenuIcon src={Menu} alt="menu" />
      </button>
      <button>
        <StyledMenuIcon src={Poll} alt="menu" />
      </button>
      <button>
        <StyledMenuIcon src={Appointment} alt="menu" />
      </button>
      <button>
        <StyledMenuIcon src={Role} alt="menu" />
      </button>
    </StyledContainer>
  );
}

export default MobileMenu;
