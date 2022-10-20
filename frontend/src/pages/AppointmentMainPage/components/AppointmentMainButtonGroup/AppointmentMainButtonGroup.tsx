import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import Button from '../../../../components/Button/Button';
import { Appointment } from '../../../../types/appointment';

type Props = {
  appointmentCode: Appointment['code'];
  isClosed: boolean;
};

function AppointmentMainButtonGroup({ appointmentCode, isClosed }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <FlexContainer gap="1.2rem" justifyContent="end">
      {!isClosed && (
        <Button
          variant="filled"
          width="8.4rem"
          padding="0.8rem 0"
          fontSize="1.2rem"
          borderRadius="5px"
          colorScheme={theme.colors.PURPLE_100}
          onClick={handleNavigate(`${appointmentCode}/progress`)}
        >
          선택하기
        </Button>
      )}
      <Button
        variant="outlined"
        width="8.4rem"
        padding="0.8rem 0"
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
