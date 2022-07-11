import { useTheme } from '@emotion/react';
import React from 'react';
import Button from '../common/Button/Button';
import FlexContainer from '../common/FlexContainer/FlexContainer';

interface Props {
  isAnonymous: boolean;
  // TODO: FUnction에 대한 Type지정
  handleAnonymous: Function;
  handleMultiplePollCountAllowed: Function;
  isMultiplePollCountAllowed: boolean;
}

function PollCreateFormButtonGroup({
  isAnonymous,
  handleAnonymous,
  isMultiplePollCountAllowed,
  handleMultiplePollCountAllowed
}: Props) {
  const theme = useTheme();

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      <FlexContainer gap="1.2rem">
        <Button
          width="6.4rem"
          height="3.6rem"
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
          height="3.6rem"
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
          height="3.6rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="0.8rem"
          colorScheme={isMultiplePollCountAllowed ? theme.colors.GRAY_400 : theme.colors.PURPLE_100}
          onClick={handleMultiplePollCountAllowed(false)}
        >
          하나만 투표 가능
        </Button>
        <Button
          width="9.2rem"
          height="3.6rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="0.8rem"
          colorScheme={isMultiplePollCountAllowed ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
          onClick={handleMultiplePollCountAllowed(true)}
        >
          여러개 투표 가능
        </Button>
      </FlexContainer>
    </FlexContainer>
  );
}

export default PollCreateFormButtonGroup;
