import React from 'react';
import styled from '@emotion/styled';
import { useTheme } from '@emotion/react';
import { useNavigate, useParams } from 'react-router-dom';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Button from '../../@common/Button/Button';
import MembersProfile from '../../MembersProfile/MembersProfile';
import { GroupInterface } from '../../../types/group';
import Divider from '../../@common/Divider/Divider';

function PollMainHeader() {
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
        <StyledTitle>투표 목록</StyledTitle>
        <Button
          width="12rem"
          colorScheme={theme.colors.PURPLE_100}
          variant="filled"
          onClick={handleNavigate('create')}
          fontSize="1.6rem"
        >
          투표 생성하기
        </Button>
      </FlexContainer>
      <Divider />
    </>
  );
}

const StyledTitle = styled.div`
  font-size: 4rem;
`;

export default PollMainHeader;
