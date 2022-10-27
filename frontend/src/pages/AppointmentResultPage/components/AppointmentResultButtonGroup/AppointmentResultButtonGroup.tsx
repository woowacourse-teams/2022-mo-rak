import { useTheme } from '@emotion/react';

import { useNavigate } from 'react-router-dom';

import { AxiosError } from 'axios';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import Tooltip from '../../../../components/Tooltip/Tooltip';
import { Group } from '../../../../types/group';
import {
  Appointment,
  GetAppointmentResponse,
  AppointmentRecommendation
} from '../../../../types/appointment';
import { closeAppointment, deleteAppointment } from '../../../../api/appointment';
import Button from '../../../../components/Button/Button';
import { getFormattedDateTime } from '../../../../utils/date';
import Question from '../../../../assets/question.svg';
import { StyledHelpIconContainer, StyledHelpIcon } from './AppointmentResultButtonGroup.styles';

type Props = {
  groupCode: Group['code'];
  appointmentCode: Appointment['code'];
  isClosed: Appointment['isClosed'];
  isHost: GetAppointmentResponse['isHost'];
  title: GetAppointmentResponse['title'];
  appointmentRecommendation: Array<AppointmentRecommendation>;
  setIsClosed: (isClosed: Appointment['isClosed']) => void;
};

function AppointmentResultButtonGroup({
  groupCode,
  appointmentCode,
  isClosed,
  isHost,
  title,
  appointmentRecommendation,
  setIsClosed
}: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleCloseAppointment = async () => {
    try {
      if (window.confirm('약속잡기를 마감하시겠습니까?')) {
        await closeAppointment(groupCode, appointmentCode);
        setIsClosed(true);
      }
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        switch (errCode) {
          case '3300': {
            alert('존재하지 않는 약속잡기입니다.');
            navigate(`/groups/${groupCode}/appointment`);

            break;
          }

          case '3100': {
            alert('마감된 약속잡기입니다.');
            setIsClosed(true);

            break;
          }
        }
      }
    }
  };

  const firstRankAppointmentRecommendations = appointmentRecommendation
    .filter(({ rank }) => rank === 1)
    .map(({ recommendStartDateTime, recommendEndDateTime }) => {
      return `${getFormattedDateTime(recommendStartDateTime)}~${getFormattedDateTime(
        recommendEndDateTime
      )}`;
    });

  const handleCreateNewPoll = () => {
    navigate(`/groups/${groupCode}/poll/create`, {
      state: {
        title,
        firstRankAppointmentRecommendations
      }
    });
  };

  const handleDeleteAppointment = async () => {
    try {
      if (window.confirm('약속잡기를 삭제하시겠습니까?')) {
        await deleteAppointment(groupCode, appointmentCode);
        navigate(`/groups/${groupCode}/appointment`);
      }
    } catch (err) {
      if (err instanceof AxiosError) {
        const errCode = err.response?.data.codeNumber;

        switch (errCode) {
          case '9902': {
            alert(
              '현재 사이트 이용이 원활하지 않아 삭제가 진행되지 않았습니다. 잠시후 이용해주세요'
            );
            navigate(`/groups/${groupCode}/appointment`);

            break;
          }

          case '3300': {
            alert('존재하지 않는 약속잡기입니다.');
            navigate(`/groups/${groupCode}/appointment`);

            break;
          }
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
              width="20rem"
              padding="2rem 0"
              fontSize="2.4rem"
              onClick={handleCloseAppointment}
            >
              마감
            </Button>
          )}

          <Button
            variant="filled"
            colorScheme={theme.colors.GRAY_400}
            width="20rem"
            padding="2rem 0"
            fontSize="2.4rem"
            onClick={handleDeleteAppointment}
          >
            삭제
          </Button>
          {isClosed && firstRankAppointmentRecommendations.length > 1 && (
            <FlexContainer alignItems="center" gap="0.8rem">
              <Button
                variant="filled"
                colorScheme={theme.colors.YELLOW_100}
                padding="2rem 3.2rem"
                fontSize="2.4rem"
                onClick={handleCreateNewPoll}
              >
                공동 1등 재투표
              </Button>
              <Tooltip
                content="공동 1등이 나왔네요! 공동 1등에 대한 재투표를 생성할 수 있습니다."
                width="24"
                placement="right"
                backgroundColor={theme.colors.GRAY_300}
              >
                <StyledHelpIconContainer>
                  <StyledHelpIcon src={Question} alt="help-icon" />
                </StyledHelpIconContainer>
              </Tooltip>
            </FlexContainer>
          )}
        </>
      )}

      {!isClosed && (
        <Button
          variant="filled"
          colorScheme={theme.colors.PURPLE_100}
          width="20rem"
          padding="2rem 0"
          fontSize="2.4rem"
          onClick={handleNavigate(`/groups/${groupCode}/appointment/${appointmentCode}/progress`)}
        >
          가능시간수정
        </Button>
      )}
    </FlexContainer>
  );
}

export default AppointmentResultButtonGroup;
