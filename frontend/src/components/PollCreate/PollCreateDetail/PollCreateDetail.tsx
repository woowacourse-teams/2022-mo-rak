import { useTheme } from '@emotion/react';

import Button from '../../@common/Button/Button';
import FlexContainer from '../../@common/FlexContainer/FlexContainer';
import { PollInterface } from '../../../types/poll';
import { memo } from 'react';

interface Props {
  isAnonymous: PollInterface['isAnonymous'];
  isAllowedMultiplePollCount: boolean;
  handleAnonymous: (anonymousStatus: boolean) => () => void;
  handleAllowedMultiplePollCount: (isAllowedMultiplePollCountStatus: boolean) => () => void;
}

function PollCreateDetail({
  isAnonymous,
  isAllowedMultiplePollCount,
  handleAnonymous,
  handleAllowedMultiplePollCount
}: Props) {
  const theme = useTheme();

  return (
    <FlexContainer flexDirection="column" gap="1.2rem">
      <FlexContainer gap="1.2rem">
        <Button
          padding="1.2rem 2rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="1.2rem"
          colorScheme={isAnonymous ? theme.colors.GRAY_400 : theme.colors.PURPLE_100}
          onClick={handleAnonymous(false)}
        >
          기명
        </Button>
        <Button
          padding="1.2rem 2rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="1.2rem"
          colorScheme={isAnonymous ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
          onClick={handleAnonymous(true)}
        >
          익명
        </Button>
      </FlexContainer>
      <FlexContainer gap="1.2rem">
        <Button
          padding="1.2rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="1.2rem"
          colorScheme={isAllowedMultiplePollCount ? theme.colors.GRAY_400 : theme.colors.PURPLE_100}
          onClick={handleAllowedMultiplePollCount(false)}
        >
          하나만 투표 가능
        </Button>
        <Button
          padding="1.2rem"
          borderRadius="20px"
          variant="outlined"
          fontSize="1.2rem"
          colorScheme={isAllowedMultiplePollCount ? theme.colors.PURPLE_100 : theme.colors.GRAY_400}
          onClick={handleAllowedMultiplePollCount(true)}
        >
          여러개 투표 가능
        </Button>
      </FlexContainer>
    </FlexContainer>
  );
}

export default memo(
  PollCreateDetail,
  (prev, next) =>
    prev.isAnonymous === next.isAnonymous &&
    prev.isAllowedMultiplePollCount === next.isAllowedMultiplePollCount
);
