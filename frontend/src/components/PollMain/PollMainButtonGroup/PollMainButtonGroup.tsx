import React from 'react';
import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import Button from '../../@common/Button/Button';
import { PollInterface } from '../../../types/poll';

interface Props {
  pollCode: PollInterface['code'];
  status: PollInterface['status'];
}

function PollMainButtonGroup({ pollCode, status }: Props) {
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
          width="6rem"
          padding="0.4rem 0"
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
        width="6rem"
        padding="0.4rem 0"
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

export default PollMainButtonGroup;
