import { MouseEventHandler } from 'react';
import styled from '@emotion/styled';
import Slack from '../../../../assets/slack.svg';

interface Props {
  onClick: MouseEventHandler<HTMLButtonElement>;
}

function SidebarSlackMenu({ onClick }: Props) {
  return (
    <StyledContainer onClick={onClick}>
      <StyledSlackLogo src={Slack} alt="slack-icon" />
      <StyledText>슬랙 채널 연동</StyledText>
    </StyledContainer>
  );
}

const StyledContainer = styled.button`
  display: flex;
  align-items: center;
  cursor: pointer;
  gap: 1.2rem;
  font-size: 1.6rem;
`;

const StyledSlackLogo = styled.img`
  width: 2rem;
`;

const StyledText = styled.p`
  font-size: 1.6rem;
`;

export default SidebarSlackMenu;
