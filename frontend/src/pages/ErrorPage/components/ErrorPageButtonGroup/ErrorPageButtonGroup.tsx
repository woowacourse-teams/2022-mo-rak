import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';

import Button from '../../../../components/Button/Button';
import FlexContainer from '../../../../components/FlexContainer/FlexContainer';

type NavigateFunctionOverload = {
  (location: string): () => void;
  (location: number): () => void;
};

function ErrorPageButtonGroup() {
  const navigate = useNavigate();
  const theme = useTheme();

  const handleNavigate: NavigateFunctionOverload = (location: any) => () => {
    navigate(location);
  };

  return (
    <FlexContainer gap="1.2rem">
      <Button
        variant="filled"
        colorScheme={theme.colors.GRAY_300}
        padding="1.6rem 3rem"
        fontSize="2.4rem"
        borderRadius="1rem"
        onClick={handleNavigate(-1)}
      >
        뒤로 가기
      </Button>
      <Button
        variant="filled"
        colorScheme={theme.colors.YELLOW_100}
        padding="1.2rem 3rem"
        fontSize="2.4rem"
        borderRadius="1rem"
        onClick={handleNavigate('/')}
      >
        메인으로 돌아가기
      </Button>
    </FlexContainer>
  );
}

export default ErrorPageButtonGroup;
