import styled from '@emotion/styled';

import { AppointmentInterface } from '../../../types/appointment';
import LinkIcon from '../../../assets/link.svg';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { writeClipboard } from '../../../utils/clipboard';
import AppointmentResultStatus from '../AppointmentResultStatus/AppointmentResultStatus';
import { GroupInterface } from '../../../types/group';

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

interface Props {
  groupCode: GroupInterface['code'];
  appointmentCode: AppointmentInterface['code'];
  title: AppointmentInterface['title'];
  closedAt: AppointmentInterface['closedAt'];
  isClosed: AppointmentInterface['isClosed'];
}

function AppointmentResultHeader({ groupCode, appointmentCode, title, isClosed, closedAt }: Props) {
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
    <>
      {/* TODO: 리팩토링 */}
      <FlexContainer gap="1.2rem">
        {/* TODO: Input 컴포넌트 width 100%에 대해 고민해보고 추후 해결되면 사용해주자 */}
        <StyledLinkIcon src={LinkIcon} alt="link" onClick={handleCopyInvitationLink} />
        <StyledTitle>{title}</StyledTitle>
      </FlexContainer>
      <FlexContainer justifyContent="space-between">
        <FlexContainer flexDirection="column" gap="0.4rem">
          <StyledContent>모락은 가장 많이 겹치는 시간을 추천해드립니다🦔</StyledContent>
          <StyledContent>
            마감기한:
            {getFormattedClosedTime(closedAt)}
            까지😀
          </StyledContent>
        </FlexContainer>
        <AppointmentResultStatus isClosed={isClosed} />
      </FlexContainer>
    </>
  );
}

const StyledTitle = styled.h1`
  font-size: 4rem;
`;

const StyledContent = styled.p`
  font-size: 2.4rem;
`;

const StyledLinkIcon = styled.img`
  width: 2.4rem;
`

export default AppointmentResultHeader;
