import { useTheme } from '@emotion/react';

import { useNavigate } from 'react-router-dom';

import { AxiosError } from 'axios';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { GroupInterface } from '../../../types/group';
import { AppointmentInterface, getAppointmentResponse, AppointmentRecommendationInterface } from '../../../types/appointment';
import { closeAppointment, deleteAppointment } from '../../../api/appointment';
import Button from '../../@common/Button/Button';
import { getFormattedDateTime } from '../../../utils/date';

interface Props {
  groupCode: GroupInterface['code'];
  appointmentCode: AppointmentInterface['code'];
  isClosed: AppointmentInterface['isClosed'];
  isHost: getAppointmentResponse['isHost'];
  title: getAppointmentResponse['title'];
  appointmentRecommendation: Array<AppointmentRecommendationInterface>; 
}

function AppointmentResultButtonGroup({ groupCode, appointmentCode, isClosed, isHost, title, appointmentRecommendation }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleCloseAppointment = () => {
    try {
      if (window.confirm('약속잡기를 마감하시겠습니까?')) {
        closeAppointment(groupCode, appointmentCode);
      }
    } catch (err) {
      console.log(err);
    }
  };

  const firstRankAppointmentRecommendations = appointmentRecommendation
  .filter(({rank}) => rank === 1)
  .map(({recommendStartDateTime, recommendEndDateTime}) => {
    return `${getFormattedDateTime(recommendStartDateTime)}~${getFormattedDateTime(recommendEndDateTime)}`;
  });
  
  const handleCreateNewPoll = async () => {
    try {
        navigate(`/groups/${groupCode}/poll/create`, {
          state: {
            title,
            firstRankAppointmentRecommendations
          }
        });
    } catch (err) {
      console.log(err);
    }
  }

  const handleDeleteAppointment = async () => {
    try {
      if (window.confirm('약속잡기를 삭제하시겠습니까?')) {
        await deleteAppointment(groupCode, appointmentCode);
        navigate(`/groups/${groupCode}/appointment`);
      }
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        if (errCode === '9902') {
          alert('현재 사이트 이용이 원활하지 않아 삭제가 진행되지 않았습니다. 잠시후 이용해주세요');
          navigate(`/groups/${groupCode}/appointment`);
        }
      }
    }
  };

return (
    <FlexContainer gap="4rem" justifyContent="center">
      {isHost && (
        <>
          {!isClosed && (
            <Button
              variant="filled"
              colorScheme={theme.colors.GRAY_400}
              width="22rem"
              padding="2rem 0"
              fontSize="3.2rem"
              onClick={handleCloseAppointment}
            >
              마감
            </Button>
          )}
          <Button
            variant="filled"
            colorScheme={theme.colors.GRAY_400}
            width="22rem"
            padding="2rem 0"
            fontSize="3.2rem"
            onClick={handleDeleteAppointment}
          >
            삭제
          </Button>
          {(isClosed && firstRankAppointmentRecommendations.length > 1) && (
            <Button
              variant="filled"
              colorScheme={theme.colors.YELLOW_100} 
              padding="2rem"
              fontSize="3.2rem"
              onClick={handleCreateNewPoll}
            >
              공동 1등에 대한 재투표 만들기 
            </Button>
          )}
        </>
      )}

      {!isClosed && (
        <Button
          variant="filled"
          colorScheme={theme.colors.PURPLE_100}
          width="22rem"
          padding="2rem 0"
          fontSize="3.2rem"
          onClick={handleNavigate(`/groups/${groupCode}/appointment/${appointmentCode}/progress`)}
        >
          가능시간수정
        </Button>
      )}
    </FlexContainer>
  );
}

export default AppointmentResultButtonGroup;
