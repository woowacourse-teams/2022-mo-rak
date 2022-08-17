import styled from '@emotion/styled';
import React from 'react';
import { AppointmentInterface } from '../../../types/appointment';
import LinkIcon from '../../../assets/link.svg';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { writeClipboard } from '../../../utils/clipboard';
import AppointmentResultStatus from '../AppointmentResultStatus/AppointmentResultStatus';

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
  title: AppointmentInterface['title'];
  closedAt: AppointmentInterface['closedAt'];
  isClosed: AppointmentInterface['isClosed'];
}

function AppointmentResultHeader({ title, closedAt, isClosed }: Props) {
  const handleCopyInviationLink = () => {
    const progressLink = `${process.env.CLIENT_URL}${
      window.location.pathname.split('/result')[0]
    }/progress`;

    writeClipboard(progressLink).then(() => {
      alert('약속잡기 진행 링크가 클립보드에 복사되었습니다📆');
    });
  };

  return (
    <>
      {/* TODO: 리팩토링 */}
      <FlexContainer gap="0.4rem">
        {/* TODO: Input 컴포넌트 width 100%에 대해 고민해보고 추후 해결되면 사용해주자 */}
        <input type="image" src={LinkIcon} alt="link" onClick={handleCopyInviationLink} />
        <StyledTitle>{title}</StyledTitle>
      </FlexContainer>
      <FlexContainer flexDirection="column" gap="0.4rem">
        <StyledContent>모락은 가장 많이 겹치는 시간을 추천해드립니다🦔</StyledContent>
        <StyledContent>
          마감기한:
          {getFormattedClosedTime(closedAt)}
          까지😀
        </StyledContent>
      <FlexContainer justifyContent="space-between">
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

export default AppointmentResultHeader;
