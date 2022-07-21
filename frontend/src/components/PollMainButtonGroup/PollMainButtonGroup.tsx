import React from 'react';
import { useTheme } from '@emotion/react';
import FlexContainer from '../common/FlexContainer/FlexContainer';
import Button from '../common/Button/Button';
import { PollInterface } from '../../types/poll';

interface Props {
  pollId: number;
  handleNavigate: (location: string) => () => void;
  status: PollInterface['status'];
}

function PollMainButtonGroup({ pollId, handleNavigate, status }: Props) {
  const theme = useTheme();

  return (
    <FlexContainer gap="1.2rem" justifyContent="end">
      {/* TODO: 컴포넌트 만들기 */}
      {status === 'OPEN' ? (
        <Button
          type="button"
          variant="filled"
          width="6rem"
          padding="0.4rem 0"
          fontSize="1rem"
          borderRadius="5px"
          color={theme.colors.WHITE_100}
          colorScheme={theme.colors.PURPLE_100}
          onClick={handleNavigate(`${pollId}/progress`)}
        >
          투표하기
        </Button>
      ) : (
        ''
      )}
      <Button
        type="button"
        variant="outlined"
        width="6rem"
        padding="0.4rem 0"
        fontSize="1rem"
        borderRadius="5px"
        color={theme.colors.PURPLE_100}
        colorScheme={theme.colors.PURPLE_100}
        onClick={handleNavigate(`${pollId}/result`)}
      >
        결과보기
      </Button>
    </FlexContainer>
  );
}

export default PollMainButtonGroup;
