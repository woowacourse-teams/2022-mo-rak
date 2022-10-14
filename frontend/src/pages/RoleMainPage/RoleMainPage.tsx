import { StyledContainer } from './RoleMainPage.styles';
import RoleMainHeader from './components/RoleMainHeader/RoleMainHeader';
import RoleMainProgress from './components/RoleMainProgress/RoleMainProgress';
import RoleMainResult from './components/RoleMainResult/RoleMainResult';

function RoleMainPage() {
  return (
    <StyledContainer>
      <RoleMainHeader />

      {/* 역할 목록 */}
      {/* 역할 정하기 진행  */}
      <RoleMainProgress />

      {/* 역할 결과 */}
      <RoleMainResult />
    </StyledContainer>
  );
}

export default RoleMainPage;
