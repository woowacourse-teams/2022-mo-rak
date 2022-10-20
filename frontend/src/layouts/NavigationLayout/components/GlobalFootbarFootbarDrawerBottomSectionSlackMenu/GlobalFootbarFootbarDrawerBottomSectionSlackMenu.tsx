import { MouseEventHandler } from 'react';
import Slack from '../../../../assets/slack.svg';
import {
  StyledContainer,
  StyledSlackLogo,
  StyledText
} from './GlobalFootbarFootbarDrawerBottomSectionSlackMenu.styles';

type Props = {
  onClick: MouseEventHandler<HTMLButtonElement>;
};

function GlobalFootbarFootbarDrawerBottomSectionSlackMenu({ onClick }: Props) {
  return (
    <StyledContainer onClick={onClick}>
      <StyledSlackLogo src={Slack} alt="slack-icon" />
      <StyledText>슬랙 채널 연동</StyledText>
    </StyledContainer>
  );
}

export default GlobalFootbarFootbarDrawerBottomSectionSlackMenu;
