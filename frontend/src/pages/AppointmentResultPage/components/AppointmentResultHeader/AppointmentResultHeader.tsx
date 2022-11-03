import { Appointment } from '@/types/appointment';
import linkImg from '@/assets/link.svg';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import { writeClipboard } from '../../../../utils/clipboard';
import AppointmentResultStatus from '../AppointmentResultStatus/AppointmentResultStatus';
import { Group } from '@/types/group';
import {
  StyledContainer,
  StyledTitle,
  StyledContent,
  StyledLinkIcon,
  StyledHelpIconContainer,
  StyledHelpIcon,
  StyledDescription
} from './AppointmentResultHeader.styles';
import Tooltip from '@/components/Tooltip/Tooltip';
import questionImg from '@/assets/question.svg';
import { useTheme } from '@emotion/react';

const getFormattedClosedTime = (value: string) => {
  const date = new Date(value);
  return date.toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    hour12: true
  });
};

type Props = {
  groupCode: Group['code'];
  appointmentCode: Appointment['code'];
  title: Appointment['title'];
  closedAt: Appointment['closedAt'];
  isClosed: Appointment['isClosed'];
};

function AppointmentResultHeader({ groupCode, appointmentCode, title, isClosed, closedAt }: Props) {
  const theme = useTheme();

  const handleCopyInvitationLink = () => {
    const baseLink = `${process.env.CLIENT_URL}/groups/${groupCode}/appointment/${appointmentCode}`;

    if (isClosed) {
      writeClipboard(`${baseLink}/result`).then(() => {
        alert('약속잡기 결과를 공유할 수 있는 링크가 클립보드에 복사되었습니다📆');
      });

      return;
    }

    writeClipboard(`${baseLink}/progress`).then(() => {
      alert('약속잡기를 진행할 수 있는 링크가 클립보드에 복사되었습니다📆');
    });
  };

  return (
    <StyledContainer>
      {/* TODO: 리팩토링 */}
      <FlexContainer gap="1.2rem">
        {/* TODO: Input 컴포넌트 width 100%에 대해 고민해보고 추후 해결되면 사용해주자 */}
        <StyledLinkIcon src={linkImg} alt="link" onClick={handleCopyInvitationLink} />
        <StyledTitle>{title}</StyledTitle>
      </FlexContainer>
      <FlexContainer justifyContent="space-between">
        <FlexContainer flexDirection="column" gap="0.4rem">
          <StyledContent>모락이 가장 많이 겹치는 시간을 추천해드립니다 🦔</StyledContent>
          <StyledContent>
            마감기한:
            {getFormattedClosedTime(closedAt)}
            까지
          </StyledContent>
          <Tooltip
            content="공동 1등이 나오면, 마감 이후 재투표를 빠르게 진행할 수 있어요!"
            width="28"
            placement="bottom"
            fontSize="1.6rem"
            backgroundColor={theme.colors.GRAY_200}
          >
            <FlexContainer gap="2rem" alignItems="center">
              <StyledDescription>공동 1등이 나오면 어떻게하나요?</StyledDescription>
              <StyledHelpIconContainer>
                <StyledHelpIcon src={questionImg} alt="help-icon" />
              </StyledHelpIconContainer>
            </FlexContainer>
          </Tooltip>
        </FlexContainer>
        <AppointmentResultStatus isClosed={isClosed} />
      </FlexContainer>
    </StyledContainer>
  );
}

export default AppointmentResultHeader;
