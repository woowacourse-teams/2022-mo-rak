import serviceLogoImg from '../../../../assets/service-logo.svg';

import GroupCreateForm from '../GroupCreateForm/GroupCreateForm';
import GroupParticipateForm from '../GroupParticipateForm/GroupParticipateForm';

import {
  StyledContainer,
  StyledTopContainer,
  StyledBottomContainer,
  StyledLogo,
  StyledBigText,
  StyledSmallText
} from './GroupInitContainer.styles';

function GroupInitContainer() {
  return (
    <StyledContainer>
      <StyledTopContainer>
        <StyledLogo src={serviceLogoImg} alt="logo" />
        <StyledBigText>새로운 그룹에 참여해볼까요?</StyledBigText>
        <StyledSmallText>
          아직 그룹이 없네요. <br />
          새로운 그룹을 생성하거나, <br />
          초대받은 그룹에 참가해서 모락을 시작해보세요!
        </StyledSmallText>
      </StyledTopContainer>
      <StyledBottomContainer>
        <GroupCreateForm />
        <GroupParticipateForm />
      </StyledBottomContainer>
    </StyledContainer>
  );
}

export default GroupInitContainer;
