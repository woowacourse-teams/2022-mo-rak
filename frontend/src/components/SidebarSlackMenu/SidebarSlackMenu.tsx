import { MouseEventHandler } from 'react';
import styled from '@emotion/styled';
import Slack from '../../assets/slack.svg';

interface Props {
  onClickMenu: MouseEventHandler<HTMLButtonElement>;
}

function SidebarSlackMenu({ onClickMenu }: Props) {
  return (
    <StyledContainer onClick={onClickMenu}>
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
  font-size: 1.7rem; // TODO: 4단위로 변경
`;

export default SidebarSlackMenu;
