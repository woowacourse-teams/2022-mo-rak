import { FormEvent } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { AxiosError } from 'axios';
import Box from '../../../../components/Box/Box';
import AppointmentCreateFormButtonGroup from '../AppointmentCreateFormButtonGroup/AppointmentCreateFormButtonGroup';
import AppointmentCreateFormTitleInput from '../AppointmentCreateFormTitleInput/AppointmentCreateFormTitleInput';
import AppointmentCreateFormDescriptionInput from '../AppointmentCreateFormDescriptionInput/AppointmentCreateFormDescriptionInput';
import AppointmentCreateFormDurationInput from '../AppointmentCreateFormDurationInput/AppointmentCreateFormDurationInput';
import AppointmentCreateFormTimeLimitInput from '../AppointmentCreateFormTimeLimitInput/AppointmentCreateFormTimeLimitInput';
import AppointmentCreateFormCloseTimeInput from '../AppointmentCreateFormCloseTimeInput/AppointmentCreateFormCloseTimeInput';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import useInput from '../../../../hooks/useInput';
import useInputs from '../../../../hooks/useInputs';
import { Time, createAppointmentData, AppointmentInterface } from '../../../../types/appointment';
import { createAppointment } from '../../../../api/appointment';
import { GroupInterface } from '../../../../types/group';
import { StyledForm } from './AppointmentCreateForm.style';

interface Props {
  startDate: AppointmentInterface['startDate'];
  endDate: AppointmentInterface['endDate'];
}

const getFormattedTime = (time: Time) => {
  const { period, hour, minute } = time;

  return `${hour.padStart(2, '0')}:${minute}${period}`;
};

const getPlusOneDate = (date: string) => {
  const currentDate = new Date(date);
  currentDate.setDate(currentDate.getDate() + 1);

  return currentDate.toISOString().split('T')[0];
};

function AppointmentCreateForm({ startDate, endDate }: Props) {
  const navigate = useNavigate();
  const [title, handleTitle] = useInput('');
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

    if (!startDate) {
      alert('달력을 활용하여 약속잡기 날짜를 지정해주세요');

      return;
    }

    const formattedStartTime = getFormattedTime(startTime);
    const formattedEndTime = getFormattedTime(endTime);

    const appointment: createAppointmentData = {
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

        if (errCode === '3103') {
          alert('진행 시간은 약속잡기 시간(가능시간제한)보다 짧아야 합니다.');

          return;
        }

        if (errCode === '3117') {
          alert('마감 시간은 현재 시간과 마지막 날짜의 최대 가능 시간사이여야합니다');

          return;
        }

        if (errCode === '3102') {
          alert('약속잡기의 마지막 날짜와 시간은 현재보다 과거일 수 없습니다..');

          return;
        }

        if (errCode === '3107') {
          alert('약속잡기 진행시간은 30분에서 24시간 사이여야 합니다.');
        }
      }
    }
  };

  // TODO: handleNavigate vs 아래처럼
  const handleNavigateAppointmentMain = () => {
    navigate(`/groups/${groupCode}/appointment`);
  };

  return (
    // TODO: StyledForm처럼 컴포넌트의 역할을 담은 네이밍을 해줄 것인지? S
    // StyledContainer처럼 최상단은 무조건 StyledContainer로 해줄 것인지 컨벤션 정해서 통일
    <StyledForm onSubmit={handleCreateAppointment}>
      <Box width="66rem" minHeight="56.4rem" padding="5.2rem">
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
      <AppointmentCreateFormButtonGroup onCancel={handleNavigateAppointmentMain} />
    </StyledForm>
  );
}

export default AppointmentCreateForm;
