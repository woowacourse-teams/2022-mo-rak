import styled from '@emotion/styled';
import React, { FormEvent } from 'react';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import Box from '../../common/Box/Box';
import AppointmentCreateFormButtonGroup from '../AppointmentCreateFormButtonGroup/AppointmentCreateFormButtonGroup';
import AppointmentCreateFormTitleInput from '../AppointmentCreateFormTitleInput/AppointmentCreateFormTitleInput';
import AppointmentCreateFormDescriptionInput from '../AppointmentCreateFormDescriptionInput/AppointmentCreateFormDescriptionInput';
import AppointmentCreateFormDurationInput from '../AppointmentCreateFormDurationInput/AppointmentCreateFormDurationInput';
import AppointmentCreateFormTimeLimitInput from '../AppointmentCreateFormTimeLimitInput/AppointmentCreateFormTimeLimitInput';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import useInput from '../../../hooks/useInput';
import useInputs from '../../../hooks/useInputs';
import { Time, CreateAppointmentRequest, AppointmentInterface } from '../../../types/appointment';
import { createAppointment } from '../../../api/appointment';
import { GroupInterface } from '../../../types/group';

interface Props {
  startDate: AppointmentInterface['startDate'];
  endDate: AppointmentInterface['endDate'];
}

const getFormattedTime = (time: Time) => {
  const { period, hour, minute } = time;

  return `${hour.padStart(2, '0')}:${minute}${period}`;
};

function AppointmentCreateForm({ startDate, endDate }: Props) {
  const navigate = useNavigate();
  const [title, handleTitle] = useInput();
  // TODO: groupCode 받아오는 게 계속 중복되어서, 중복줄이자
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };
  const [description, handleDescription] = useInput();
  const [duration, handleDuration] = useInputs<Omit<Time, 'period'>>({ hour: '', minute: '' });
  const [startTime, handleStartTime] = useInputs<Time>({ period: 'AM', hour: '', minute: '' });
  const [endTime, handleEndTime] = useInputs<Time>({ period: 'AM', hour: '', minute: '' });

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const appointment: CreateAppointmentRequest = {
      title,
      description,
      startDate,
      endDate,
      startTime: getFormattedTime(startTime),
      endTime: getFormattedTime(endTime),
      durationHours: Number(duration.hour),
      durationMinutes: Number(duration.minute)
    };

    try {
      const res = await createAppointment(groupCode, appointment);
      const appointmentCode = res.headers.get('location').split('appointments/')[1];

      navigate(`/groups/${groupCode}/appointment/${appointmentCode}/progress`);
      console.log(appointmentCode);
    } catch (error) {
      console.log(error);
    }
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
            endTime={endTime}
            onChangeStartTime={handleStartTime}
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
