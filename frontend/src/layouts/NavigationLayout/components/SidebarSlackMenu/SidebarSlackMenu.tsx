import { MouseEventHandler } from 'react';
import Slack from '../../../../assets/slack.svg';
import { StyledContainer, StyledSlackLogo, StyledText } from './SidebarSlackMenu.styles';

type Props = {
  onClick: MouseEventHandler<HTMLButtonElement>;
};

function SidebarSlackMenu({ onClick }: Props) {
  return (
    <StyledContainer onClick={onClick}>
      <StyledSlackLogo src={Slack} alt="slack-icon" />
      <StyledText>슬랙 채널 연동</StyledText>
    </StyledContainer>
  );
}

export default SidebarSlackMenu;
