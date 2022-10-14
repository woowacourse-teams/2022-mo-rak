import { StyledContainer } from './RoleMainPage.styles';
import RoleMainHeader from './components/RoleMainHeader/RoleMainHeader';
import RoleMainContainer from './components/RoleMainContainer/RoleMainContainer';

function RoleMainPage() {
  return (
    <StyledContainer>
      <RoleMainHeader />
      <RoleMainContainer />
    </StyledContainer>
  );
}

export default RoleMainPage;
