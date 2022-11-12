import { MouseEventHandler } from 'react';

import {
  StyledContainer,
  StyledSlackLogo,
  StyledText
} from '@/layouts/NavigationLayout/components/SidebarSlackMenu/SidebarSlackMenu.styles';

import slackImg from '@/assets/slack.svg';

type Props = {
  onClick: MouseEventHandler<HTMLButtonElement>;
};

function SidebarSlackMenu({ onClick }: Props) {
  return (
    <StyledContainer onClick={onClick}>
      <StyledSlackLogo src={slackImg} alt="slack-icon" />
      <StyledText>슬랙 채널 연동</StyledText>
    </StyledContainer>
  );
}

export default SidebarSlackMenu;
