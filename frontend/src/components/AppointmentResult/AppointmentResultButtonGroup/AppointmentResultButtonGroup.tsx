import React from 'react';
import { useTheme } from '@emotion/react';

import { useNavigate } from 'react-router-dom';

import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { GroupInterface } from '../../../types/group';
import { AppointmentInterface } from '../../../types/appointment';
import { closeAppointment } from '../../../api/appointment';
import Button from '../../@common/Button/Button';

interface Props {
  groupCode: GroupInterface['code'];
  appointmentCode: AppointmentInterface['code'];
}

function AppointmentResultButtonGroup({ groupCode, appointmentCode }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const handleCloseAppointment = async () => {
    try {
      if (window.confirm('약속잡기를 삭제하시겠습니까?')) {
        const res = await closeAppointment(groupCode, appointmentCode);
        console.log(res);
      }
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <FlexContainer gap="4rem">
      <Button
        variant="filled"
        colorScheme={theme.colors.GRAY_300}
        width="30rem"
        padding="2.4rem"
        fontSize="4rem"
        onClick={handleCloseAppointment}
      >
        마감
      </Button>
      <Button
        variant="filled"
        colorScheme={theme.colors.PURPLE_100}
        width="30rem"
        padding="2.4rem"
        fontSize="4rem"
        onClick={handleNavigate(`/groups/${groupCode}/appointment/${appointmentCode}/progress`)}
      >
        가능시간수정
      </Button>
    </FlexContainer>
  );
}

export default AppointmentResultButtonGroup;
