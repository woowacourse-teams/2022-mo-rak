import styled from '@emotion/styled';
import { ChangeEventHandler, memo } from 'react';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { Time } from '../../../types/appointment';
import AppointmentCreateFormTimeInput from '../AppointmentCreateFormTimeInput/AppointmentCreateFormTimeInput';
import Question from '../../../assets/question.svg';
import Tooltip from '../../@common/Tooltip/Tooltip';

interface Props {
  startTime: Time;
  endTime: Time;
  onChangeStartTime: ChangeEventHandler<HTMLSelectElement>;
  onChangeEndTime: ChangeEventHandler<HTMLSelectElement>;
}

function AppointmentCreateFormTimeLimitInput({
  startTime,
  endTime,
  onChangeStartTime,
  onChangeEndTime
}: Props) {
  return (
    <>
      <FlexContainer gap="2rem">
        <StyledTitle>가능 시간 제한 설정</StyledTitle>
        <Tooltip
          content="오전 12:00 ~ 오전 12:00(다음날)은 하루종일을 의미합니다."
          width="26"
          placement="right"
        >
          <StyledHelpIconWrapper>
            <StyledHelpIcon src={Question} alt="help-icon" />
          </StyledHelpIconWrapper>
        </Tooltip>
      </FlexContainer>

      <FlexContainer alignItems="center" gap="2.8rem">
        <AppointmentCreateFormTimeInput time={startTime} onChange={onChangeStartTime} />
        <StyledContent>~</StyledContent>
        <AppointmentCreateFormTimeInput time={endTime} onChange={onChangeEndTime} />
      </FlexContainer>
    </>
  );
}

const StyledTitle = styled.div(
  ({ theme }) => `
  font-size: 2.8rem;
  color: ${theme.colors.PURPLE_100};
`
);

const StyledHelpIconWrapper = styled.div(
  ({ theme }) => `
  display: flex;
  justify-content: center;
  width: 2.8rem;
  height: 2.8rem;
  background: ${theme.colors.GRAY_300};
  border-radius: 100%;
`
);

const StyledHelpIcon = styled.img`
  width: 2rem;
`;

const StyledContent = styled.p`
  font-size: 2.4rem;
`;

export default memo(
  AppointmentCreateFormTimeLimitInput,
  (prev, next) => prev.endTime === next.endTime && prev.startTime === next.startTime
);
