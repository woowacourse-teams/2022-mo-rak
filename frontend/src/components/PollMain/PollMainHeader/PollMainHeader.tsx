import React from 'react';
import { useTheme } from '@emotion/react';
import { useNavigate, useParams } from 'react-router-dom';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import Button from '../../common/Button/Button';
import MembersProfile from '../../MembersProfile/MembersProfile';
import { GroupInterface } from '../../../types/group';

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
      <FlexContainer justifyContent="end">
        <Button
          width="8rem"
          colorScheme={theme.colors.PURPLE_100}
          variant="filled"
          onClick={handleNavigate('create')}
        >
          투표 생성하기
        </Button>
      </FlexContainer>
    </>
  );
}

export default PollMainHeader;
