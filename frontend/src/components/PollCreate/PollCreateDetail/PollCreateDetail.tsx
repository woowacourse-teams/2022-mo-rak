import { useTheme } from '@emotion/react';
import React from 'react';
import Button from '../../common/Button/Button';
import FlexContainer from '../../common/FlexContainer/FlexContainer';
import { PollInterface } from '../../../types/poll';

interface Props {
  isAnonymous: PollInterface['isAnonymous'];
  isAllowedMultiplePollCount: boolean;
  // TODO: type지정 개선
  handleAnonymous: (anonymousStatus: boolean) => () => void;
  handleAllowedMultiplePollCount: (isAllowedMultiplePollCountStatus: boolean) => () => void;
}

function PollCreateDetail({
  isAnonymous,
  handleAnonymous,
  isAllowedMultiplePollCount,
  handleAllowedMultiplePollCount
}: Props) {
  const theme = useTheme();

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      <FlexContainer gap="1.2rem">
        <Button
          width="6.4rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="0.8rem"
          colorScheme={isAnonymous ? theme.colors.GRAY_400 : theme.colors.PURPLE_100}
          onClick={handleAnonymous(false)}
        >
          기명
        </Button>
        <Button
          width="6.4rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="0.8rem"
          // TODO:
          colorScheme={isAnonymous ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
          onClick={handleAnonymous(true)}
        >
          익명
        </Button>
      </FlexContainer>
      <FlexContainer gap="1.2rem">
        <Button
          width="9.2rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="0.8rem"
          colorScheme={isAllowedMultiplePollCount ? theme.colors.GRAY_400 : theme.colors.PURPLE_100}
          onClick={handleAllowedMultiplePollCount(false)}
        >
          하나만 투표 가능
        </Button>
        <Button
          width="9.2rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="0.8rem"
          colorScheme={isAllowedMultiplePollCount ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
          onClick={handleAllowedMultiplePollCount(true)}
        >
          여러개 투표 가능
        </Button>
      </FlexContainer>
    </FlexContainer>
  );
}

export default PollCreateDetail;
