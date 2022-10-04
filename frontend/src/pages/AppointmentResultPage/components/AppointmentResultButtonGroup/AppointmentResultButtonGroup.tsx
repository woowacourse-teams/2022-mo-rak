import { useTheme } from '@emotion/react';
import styled from '@emotion/styled';

import { useNavigate } from 'react-router-dom';

import { AxiosError } from 'axios';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import Tooltip from '../../../../components/Tooltip/Tooltip';
import { GroupInterface } from '../../../../types/group';
import {
  AppointmentInterface,
  getAppointmentResponse,
  AppointmentRecommendationInterface
} from '../../../../types/appointment';
import {
  closeAppointment,
  deleteAppointment,
  getAppointmentStatus
} from '../../../../api/appointment';
import Button from '../../../../components/Button/Button';
import { getFormattedDateTime } from '../../../../utils/date';
import Question from '../../../../assets/question.svg';

interface Props {
  groupCode: GroupInterface['code'];
  appointmentCode: AppointmentInterface['code'];
  isClosed: AppointmentInterface['isClosed'];
  isHost: getAppointmentResponse['isHost'];
  title: getAppointmentResponse['title'];
  appointmentRecommendation: Array<AppointmentRecommendationInterface>;
  setIsClosed: (isClosed: AppointmentInterface['isClosed']) => void;
}

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
        const {
          data: { status }
        } = await getAppointmentStatus(groupCode, appointmentCode);

        const isClosed = status === 'CLOSED' ? true : false;
        setIsClosed(isClosed);
      }
    } catch (err) {
      console.log(err);
    }
  };

  const firstRankAppointmentRecommendations = appointmentRecommendation
    .filter(({ rank }) => rank === 1)
    .map(({ recommendStartDateTime, recommendEndDateTime }) => {
      return `${getFormattedDateTime(recommendStartDateTime)}~${getFormattedDateTime(
        recommendEndDateTime
      )}`;
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
          {isClosed && firstRankAppointmentRecommendations.length > 1 && (
            <FlexContainer alignItems="center" gap="0.8rem">
              <Button
                variant="filled"
                colorScheme={theme.colors.YELLOW_100}
                padding="2rem 3.2rem"
                fontSize="3.2rem"
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
                <StyledHelpIconWrapper>
                  <StyledHelpIcon src={Question} alt="help-icon" />
                </StyledHelpIconWrapper>
              </Tooltip>
            </FlexContainer>
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

export default AppointmentResultButtonGroup;
