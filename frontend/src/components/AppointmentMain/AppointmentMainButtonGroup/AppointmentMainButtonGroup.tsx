import React from 'react';
import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Button from '../../@common/Button/Button';
import { AppointmentInterface } from '../../../types/appointment';

interface Props {
  appointmentCode: AppointmentInterface['code'];
  isClosed: boolean;
}

function AppointmentMainButtonGroup({ appointmentCode, isClosed }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  const isOpen = (isClosed: boolean) => !isClosed;

  return (
    <FlexContainer gap="1.2rem" justifyContent="end">
      {isOpen(isClosed) && (
        <Button
          type="button"
          variant="filled"
          width="6rem"
          padding="0.4rem 0"
          fontSize="1.2rem"
          borderRadius="5px"
          colorScheme={theme.colors.PURPLE_100}
          onClick={handleNavigate(`${appointmentCode}/progress`)}
        >
          선택하기
        </Button>
      )}
      <Button
        type="button"
        variant="outlined"
        width="6rem"
        padding="0.4rem 0"
        fontSize="1.2rem"
        borderRadius="5px"
        colorScheme={theme.colors.PURPLE_100}
        onClick={handleNavigate(`${appointmentCode}/result`)}
      >
        결과보기
      </Button>
    </FlexContainer>
  );
}

export default AppointmentMainButtonGroup;
