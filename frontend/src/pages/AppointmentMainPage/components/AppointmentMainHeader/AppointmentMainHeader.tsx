import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';

import Button from '../../../../components/Button/Button';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';
import Divider from '../../../../components/Divider/Divider';

function AppointmentMainHeader() {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <>
      <FlexContainer justifyContent="space-between">
        <StyledTitle>약속잡기 목록</StyledTitle>
        <Button
          width="16rem"
          colorScheme={theme.colors.PURPLE_100}
          variant="filled"
          onClick={handleNavigate('create')}
          fontSize="2rem"
          padding="1.6rem 0"
        >
          약속 생성하기
        </Button>
      </FlexContainer>
      <Divider />
    </>
  );
}

const StyledTitle = styled.div`
  font-size: 4rem;
`;

export default AppointmentMainHeader;
