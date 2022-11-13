import { FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { AxiosError } from 'axios';
import Box from '@/components/Box/Box';
import AppointmentCreateFormButtons from '@/pages/AppointmentCreatePage/components/AppointmentCreateFormButtons/AppointmentCreateFormButtons';
import AppointmentCreateFormTitleInput from '@/pages/AppointmentCreatePage/components/AppointmentCreateFormTitleInput/AppointmentCreateFormTitleInput';
import AppointmentCreateFormDescriptionInput from '@/pages/AppointmentCreatePage/components/AppointmentCreateFormDescriptionInput/AppointmentCreateFormDescriptionInput';
import AppointmentCreateFormDurationInput from '@/pages/AppointmentCreatePage/components/AppointmentCreateFormDurationInput/AppointmentCreateFormDurationInput';
import AppointmentCreateFormTimeLimitInput from '@/pages/AppointmentCreatePage/components/AppointmentCreateFormTimeLimitInput/AppointmentCreateFormTimeLimitInput';
import AppointmentCreateFormCloseTimeInput from '@/pages/AppointmentCreatePage/components/AppointmentCreateFormCloseTimeInput/AppointmentCreateFormCloseTimeInput';
import FlexContainer from '@/components/FlexContainer/FlexContainer';
import useInput from '@/hooks/useInput';
import useInputs from '@/hooks/useInputs';
import { Time, CreateAppointmentRequest, Appointment } from '@/types/appointment';
import { createAppointment } from '@/api/appointment';
import { Group } from '@/types/group';
import { StyledContainer } from '@/pages/AppointmentCreatePage/components/AppointmentCreateForm/AppointmentCreateForm.styles';
import { APPOINTMENT_ERROR } from '@/constants/errorMessage';

const getFormattedTime = (time: Time) => {
  const { period, hour, minute } = time;

  return `${hour.padStart(2, '0')}:${minute}${period}`;
};

const getPlusOneDate = (date: string) => {
  const currentDate = new Date(date);
  currentDate.setDate(currentDate.getDate() + 1);

  return currentDate.toISOString().split('T')[0];
};

type Props = {
  startDate: Appointment['startDate'];
  endDate: Appointment['endDate'];
};

function AppointmentCreateForm({ startDate, endDate }: Props) {
  const navigate = useNavigate();
  const [title, handleTitle] = useInput('');
  const { groupCode } = useParams() as { groupCode: Group['code'] };
  const [description, handleDescription] = useInput('');
  const [duration, handleDuration] = useInputs<Omit<Time, 'period'>>({ hour: '', minute: '' });
  const [startTime, handleStartTime] = useInputs<Time>({ period: 'AM', hour: '', minute: '00' });
  const [endTime, handleEndTime] = useInputs<Time>({ period: 'AM', hour: '', minute: '00' });
  const [closeDate, handleCloseDate] = useInput('');
  const [closeTime, handleCloseTime] = useInput('');

  const handleCreateAppointment = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!startDate) {
      alert(APPOINTMENT_ERROR.EMPTY_DATE);

      return;
    }

    const formattedStartTime = getFormattedTime(startTime);
    const formattedEndTime = getFormattedTime(endTime);

    const appointment: CreateAppointmentRequest = {
      title,
      description,
      startDate,
      endDate:
        formattedEndTime === '12:00AM'
          ? getPlusOneDate(endDate || startDate)
          : endDate || startDate,
      startTime: formattedStartTime,
      endTime: formattedEndTime,
      durationHours: Number(duration.hour),
      durationMinutes: Number(duration.minute),
      closedAt: `${closeDate}T${closeTime}`
    };

    try {
      const res = await createAppointment(groupCode, appointment);
      const appointmentCode = res.headers.location.split('appointments/')[1];

      navigate(`/groups/${groupCode}/appointment/${appointmentCode}/progress`);
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        switch (errCode) {
          case '3103': {
            alert(APPOINTMENT_ERROR.INVALID_DURATION_INPUT);

            break;
          }

          case '3117': {
            alert(APPOINTMENT_ERROR.INVALID_CLOSE_TIME_INPUT);

            break;
          }

          case '3102': {
            alert(APPOINTMENT_ERROR.INVALID_LAST_DATE_AND_TIME);

            break;
          }

          case '3107': {
            alert(APPOINTMENT_ERROR.UNFORMATTED_DURATION_INPUT);

            break;
          }

          case '4000': {
            alert(APPOINTMENT_ERROR.EMPTY_TITLE_AND_DESCRIPTION);

            break;
          }
        }
      }
    }
  };

  // TODO: handleNavigate vs 아래처럼
  const handleNavigateAppointmentMain = () => {
    navigate(`/groups/${groupCode}/appointment`);
  };

  return (
    <StyledContainer onSubmit={handleCreateAppointment}>
      <Box width="100%" padding="4.8rem" height="60rem">
        <FlexContainer flexDirection="column" gap="2rem">
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
            closeDate={closeDate}
            maxCloseDate={
              // TODO: 너무 복잡하다, getFormattedTime이 계속 중복되어서 사용된다.ㅠㅠ
              endDate && getFormattedTime(endTime) === '12:00AM' ? getPlusOneDate(endDate) : endDate
            }
            onChangeTime={handleCloseTime}
            onChangeDate={handleCloseDate}
          />
        </FlexContainer>
      </Box>
      {/* NOTE: onCancel로 받아주는 게 맞을까? */}
      <AppointmentCreateFormButtons onCancel={handleNavigateAppointmentMain} />
    </StyledContainer>
  );
}

export default AppointmentCreateForm;
