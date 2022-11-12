import { useNavigate } from 'react-router-dom';

import Button from '@/components/Button/Button';
import FlexContainer from '@/components/FlexContainer/FlexContainer';

import { Poll } from '@/types/poll';
import { useTheme } from '@emotion/react';

type Props = {
  pollCode: Poll['code'];
  status: Poll['status'];
};

function PollMainButtons({ pollCode, status }: Props) {
  const theme = useTheme();
  const navigate = useNavigate();

  const handleNavigate = (location: string) => () => {
    navigate(location);
  };

  return (
    <FlexContainer gap="1.2rem" justifyContent="end">
      {status === 'OPEN' && (
        <Button
          type="button"
          variant="filled"
          width="24%"
          padding="0.8rem 0"
          fontSize="1.2rem"
          borderRadius="5px"
          colorScheme={theme.colors.PURPLE_100}
          onClick={handleNavigate(`${pollCode}/progress`)}
        >
          투표하기
        </Button>
      )}
      <Button
        type="button"
        variant="outlined"
        width="24%"
        padding="0.8rem 0"
        fontSize="1.2rem"
        borderRadius="5px"
        colorScheme={theme.colors.PURPLE_100}
        onClick={handleNavigate(`${pollCode}/result`)}
        aria-label={status}
      >
        결과보기
      </Button>
    </FlexContainer>
  );
}

export default PollMainButtons;
