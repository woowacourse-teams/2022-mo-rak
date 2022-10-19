import { ChangeEventHandler, memo } from 'react';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import { Time } from '../../../../types/appointment';
import AppointmentCreateFormTimeInput from '../AppointmentCreateFormTimeInput/AppointmentCreateFormTimeInput';
import Question from '../../../../assets/question.svg';
import Tooltip from '../../../../components/Tooltip/Tooltip';
import {
  StyledTitle,
  StyledHelpIconContainer,
  StyledHelpIcon,
  StyledContent
} from './AppointmentCreateFormTimeLimitInput.styles';

type Props = {
  startTime: Time;
  endTime: Time;
  onChangeStartTime: ChangeEventHandler<HTMLSelectElement>;
  onChangeEndTime: ChangeEventHandler<HTMLSelectElement>;
};

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
          <StyledHelpIconContainer>
            <StyledHelpIcon src={Question} alt="help-icon" />
          </StyledHelpIconContainer>
        </Tooltip>
      </FlexContainer>

      <FlexContainer alignItems="center" gap="2.8rem">
        <AppointmentCreateFormTimeInput
          time={startTime}
          // TODO: 네이밍 고민
          ariaLabelHelper="start"
          onChange={onChangeStartTime}
        />
        <StyledContent>~</StyledContent>
        <AppointmentCreateFormTimeInput
          time={endTime}
          ariaLabelHelper="end"
          onChange={onChangeEndTime}
        />
      </FlexContainer>
    </>
  );
}

export default memo(
  AppointmentCreateFormTimeLimitInput,
  (prev, next) => prev.endTime === next.endTime && prev.startTime === next.startTime
);
