import styled from '@emotion/styled';
import React, { FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import Box from '../../@common/Box/Box';
import AppointmentCreateFormButtonGroup from '../AppointmentCreateFormButtonGroup/AppointmentCreateFormButtonGroup';
import AppointmentCreateFormTitleInput from '../AppointmentCreateFormTitleInput/AppointmentCreateFormTitleInput';
import AppointmentCreateFormDescriptionInput from '../AppointmentCreateFormDescriptionInput/AppointmentCreateFormDescriptionInput';
import AppointmentCreateFormDurationInput from '../AppointmentCreateFormDurationInput/AppointmentCreateFormDurationInput';
import AppointmentCreateFormTimeLimitInput from '../AppointmentCreateFormTimeLimitInput/AppointmentCreateFormTimeLimitInput';
import AppointmentCreateFormCloseTimeInput from '../AppointmentCreateFormCloseTimeInput/AppointmentCreateFormCloseTimeInput';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import useInput from '../../../hooks/useInput';
import useInputs from '../../../hooks/useInputs';
import { Time, createAppointmentData, AppointmentInterface } from '../../../types/appointment';
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
  const [description, handleDescription] = useInput('');
  const [duration, handleDuration] = useInputs<Omit<Time, 'period'>>({ hour: '', minute: '' });
  const [startTime, handleStartTime] = useInputs<Time>({ period: 'AM', hour: '', minute: '00' });
  const [endTime, handleEndTime] = useInputs<Time>({ period: 'AM', hour: '', minute: '00' });
  const [closeDate, handleCloseDate] = useInput('');
  const [closeTime, handleCloseTime] = useInput('');

  const handleCreateAppointment = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const appointment: createAppointmentData = {
      title,
      description,
      startDate,
      endDate: endDate || startDate,
      startTime: getFormattedTime(startTime),
      endTime: getFormattedTime(endTime),
      durationHours: Number(duration.hour),
      durationMinutes: Number(duration.minute),
      closedAt: `${closeDate}T${closeTime}`
    };

    try {
      const res = await createAppointment(groupCode, appointment);
      const appointmentCode = res.headers.location.split('appointments/')[1];

      navigate(`/groups/${groupCode}/appointment/${appointmentCode}/progress`);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <StyledForm onSubmit={handleCreateAppointment}>
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
          <AppointmentCreateFormCloseTimeInput
            closeTime={closeTime}
            onChangeTime={handleCloseTime}
            closeDate={closeDate}
            onChangeDate={handleCloseDate}
            maxCloseDate={endDate}
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
