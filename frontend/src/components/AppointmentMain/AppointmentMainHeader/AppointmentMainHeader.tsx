import React from 'react';
import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';
import { useNavigate, useParams } from 'react-router-dom';
import { GroupInterface } from '../../../types/group';

import Button from '../../@common/Button/Button';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import MembersProfile from '../../MembersProfile/MembersProfile';
import Divider from '../../@common/Divider/Divider';

function AppointmentMainHeader() {
  const theme = useTheme();
  const navigate = useNavigate();
  const { groupCode } = useParams() as { groupCode: GroupInterface['code'] };

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <>
      <MembersProfile groupCode={groupCode} />
      <FlexContainer justifyContent="space-between">
        <StyledTitle>약속잡기 목록</StyledTitle>
        <Button
          width="12rem"
          colorScheme={theme.colors.PURPLE_100}
          variant="filled"
          onClick={handleNavigate('create')}
          fontSize="1.6rem"
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
