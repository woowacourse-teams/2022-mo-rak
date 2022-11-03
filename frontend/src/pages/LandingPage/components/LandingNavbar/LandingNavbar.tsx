import {
  StyledMenu,
  StyledNavbar
} from '@/pages/LandingPage/components/LandingNavbar/LandingNavbar.styles';

function LandingNavbar() {
  return (
    <StyledNavbar>
      <StyledMenu href="#main-section">LOGIN</StyledMenu>
      <StyledMenu href="#service-introduction-section">ABOUT</StyledMenu>
      <StyledMenu href="#feature-introduction-section">FEATURES</StyledMenu>
    </StyledNavbar>
  );
}

export default LandingNavbar;
