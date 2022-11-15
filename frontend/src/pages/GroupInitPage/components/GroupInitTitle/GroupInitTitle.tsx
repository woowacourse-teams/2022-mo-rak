import {
  StyledContainer,
  StyledLogo,
  StyledTitle,
  StyledDescription
} from '@/pages/GroupInitPage/components/GroupInitTitle/GroupInitTitle.styles';
import serviceLogoImg from '@/assets/service-logo.svg';

function GroupInitTitle() {
  return (
    <StyledContainer>
      <StyledLogo src={serviceLogoImg} alt="모락 로고" aria-hidden="true" />
      <StyledTitle>새로운 그룹에 참여해볼까요?</StyledTitle>
      <StyledDescription>
        아직 그룹이 없네요. 새로운 그룹을 생성하거나, 초대받은 그룹에 참가해서 모락을 시작해보세요!
      </StyledDescription>
    </StyledContainer>
  );
}

export default GroupInitTitle;
