import React from 'react';
import styled from '@emotion/styled';

import Logo from '../../../assets/logo.svg';

import GroupCreateForm from '../GroupCreateForm/GroupCreateForm';
import GroupParticipateForm from '../GroupParticipateForm/GroupParticipateForm';

function GroupInitContainer() {
  return (
    <StyledContainer>
      <StyledTopContainer>
        <StyledLogo src={Logo} alt="logo" />
        <StyledBigText>새로운 그룹에 참여해볼까요?</StyledBigText>
        <StyledSmallText>아직 그룹이 없네요. 새로운 그룹을 생성하거나, 초대받은 그룹에 참가해서 모락을 시작해보세요</StyledSmallText>
      </StyledTopContainer>
      <StyledBottomContainer>
        <GroupCreateForm />
        <GroupParticipateForm />
      </StyledBottomContainer>
    </StyledContainer>
  );
}

const StyledContainer = styled.div`
  height: 100vh;
  width: 100vw;
`;

const StyledTopContainer = styled.div`
  height: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  align-items: center;
`;

const StyledBottomContainer = styled.div(({ theme }) => `
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50%;
  background: ${theme.colors.YELLOW_50};
  flex-direction: column;
  gap: 6.8rem;
`);

const StyledLogo = styled.img`
  width: 22rem;
  margin-bottom: 4rem; 
`;

const StyledBigText = styled.div`
  font-size: 4.4rem;
  margin-bottom: 2rem;
`;

const StyledSmallText = styled.div`
  font-size: 2.4rem;
`;

export default GroupInitContainer;
