import styled from '@emotion/styled';
import React, { FormEvent } from 'react';
import Box from '../../common/Box/Box';
import AppointmentCreateFormButtonGroup from '../AppointmentCreateFormButtonGroup/AppointmentCreateFormButtonGroup';
import AppointmentCreateFormTitleInput from '../AppointmentCreateFormTitleInput/AppointmentCreateFormTitleInput';
import AppointmentCreateFormDescriptionInput from '../AppointmentCreateFormDescriptionInput/AppointmentCreateFormDescriptionInput';
import AppointmentCreateFormDurationInput from '../AppointmentCreateFormDurationInput/AppointmentCreateFormDurationInput';
import AppointmentCreateFormTimeLimitInput from '../AppointmentCreateFormTimeLimitInput/AppointmentCreateFormTimeLimitInput';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import useInput from '../../../hooks/useInput';
import useInputs from '../../../hooks/useInputs';
import { Time, CreateAppointmentRequest } from '../../../types/appointment';

const getFormattedTime = (time: Time) => {
  const { period, hour, minute } = time;

  return `${hour}:${minute}${period}`;
};

function AppointmentCreateForm() {
  const [title, handleTitle] = useInput();
  const [description, handleDescription] = useInput();
  const [duration, handleDuration] = useInputs<Omit<Time, 'period'>>({ hour: '', minute: '' });
  const [startTime, handleStartTime] = useInputs<Time>({ period: 'AM', hour: '', minute: '' });
  const [endTime, handleEndTime] = useInputs<Time>({ period: 'AM', hour: '', minute: '' });

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const appointment: CreateAppointmentRequest = {
      title,
      description,
      startDate: '2022-07-26',
      endDate: '2022-08-04',
      startTime: getFormattedTime(startTime),
      endTime: getFormattedTime(endTime),
      durationHour: Number(duration.hour),
      durationMinute: Number(duration.minute)
    };

    console.log(appointment);
  };

  return (
    <StyledForm onSubmit={handleSubmit}>
      <Box width="66rem" minHeight="56.4rem" padding="4.8rem">
        <FlexContainer flexDirection="column" gap="1.6rem">
          <AppointmentCreateFormTitleInput title={title} onChange={handleTitle} />
          <AppointmentCreateFormDescriptionInput
            description={description}
            onChange={handleDescription}
          />
          <AppointmentCreateFormDurationInput duration={duration} onChange={handleDuration} />
          <AppointmentCreateFormTimeLimitInput
            startTime={startTime}
            onChangeStartTime={handleStartTime}
            endTime={endTime}
            onChangeEndTime={handleEndTime}
          />
        </FlexContainer>
      </Box>
      <AppointmentCreateFormButtonGroup />
    </StyledForm>
  );
}

const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 3.2rem;
`;

export default AppointmentCreateForm;
