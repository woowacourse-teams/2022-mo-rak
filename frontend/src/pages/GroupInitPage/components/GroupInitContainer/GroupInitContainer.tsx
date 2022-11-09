import serviceLogoImg from '@/assets/service-logo.svg';

import GroupCreateForm from '@/pages/GroupInitPage/components/GroupCreateForm/GroupCreateForm';
import GroupParticipateForm from '@/pages/GroupInitPage/components/GroupParticipateForm/GroupParticipateForm';

import {
  StyledContainer,
  StyledTopContainer,
  StyledBottomContainer,
  StyledLogo,
  StyledTitle,
  StyledDescription
} from '@/pages/GroupInitPage/components/GroupInitContainer/GroupInitContainer.styles';

function GroupInitContainer() {
  return (
    <StyledContainer>
      <StyledTopContainer>
        <StyledLogo src={serviceLogoImg} alt="모락 로고" aria-hidden="true" />
        <StyledTitle>새로운 그룹에 참여해볼까요?</StyledTitle>
        <StyledDescription>
          아직 그룹이 없네요. 새로운 그룹을 생성하거나, 초대받은 그룹에 참가해서 모락을
          시작해보세요!
        </StyledDescription>
      </StyledTopContainer>
      <StyledBottomContainer>
        <GroupCreateForm />
        <GroupParticipateForm />
      </StyledBottomContainer>
    </StyledContainer>
  );
}

export default GroupInitContainer;
