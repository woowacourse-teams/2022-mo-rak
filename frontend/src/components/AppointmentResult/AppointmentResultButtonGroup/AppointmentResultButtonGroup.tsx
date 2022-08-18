import React from 'react';
import { useTheme } from '@emotion/react';

import { useNavigate } from 'react-router-dom';

import { AxiosError } from 'axios';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { GroupInterface } from '../../../types/group';
import { AppointmentInterface, getAppointmentResponse } from '../../../types/appointment';
import { closeAppointment, deleteAppointment } from '../../../api/appointment';
import Button from '../../@common/Button/Button';

interface Props {
  groupCode: GroupInterface['code'];
  appointmentCode: AppointmentInterface['code'];
  isClosed: AppointmentInterface['isClosed'];
  isHost: getAppointmentResponse['isHost'];
}

function AppointmentResultButtonGroup({ groupCode, appointmentCode, isClosed, isHost }: Props) {
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
