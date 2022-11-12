import { useNavigate } from 'react-router-dom';

import { StyledTitle } from '@/pages/PollMainPage/components/PollMainHeader/PollMainHeader.styles';

import Button from '@/components/Button/Button';
import Divider from '@/components/Divider/Divider';
import FlexContainer from '@/components/FlexContainer/FlexContainer';

import { useTheme } from '@emotion/react';

function PollMainHeader() {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <>
      <FlexContainer justifyContent="space-between">
        <StyledTitle>투표 목록</StyledTitle>
        <Button
          width="16rem"
          colorScheme={theme.colors.PURPLE_100}
          variant="filled"
          onClick={handleNavigate('create')}
          fontSize="2rem"
          padding="1.6rem 0"
        >
          투표 생성하기
        </Button>
      </FlexContainer>
      <Divider />
    </>
  );
}

export default PollMainHeader;
