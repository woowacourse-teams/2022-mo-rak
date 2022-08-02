import React from 'react';
import { useTheme } from '@emotion/react';

import Button from '../common/Button/Button';
import FlexContainer from '../common/FlexContainer/FlexContainer';

function AppointmentResultButtonGroup() {
  const theme = useTheme();

  return (
    <FlexContainer gap="4rem">
      <Button variant="filled" colorScheme={theme.colors.GRAY_300} width="30rem" padding="2.4rem" fontSize="4rem">마감</Button>
      <Button variant="filled" colorScheme={theme.colors.PURPLE_100} width="30rem" padding="2.4rem" fontSize="4rem">가능시간수정</Button>
    </FlexContainer>
  );
}

export default AppointmentResultButtonGroup;
