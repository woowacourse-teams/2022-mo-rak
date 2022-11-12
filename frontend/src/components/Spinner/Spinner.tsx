import { useLottie } from 'lottie-react';
import { CSSProperties } from 'react';

import { StyledContainer } from '@/components/Spinner/Spinner.styles';

import spinnerLottie from '@/assets/spinner-animation.json';

type Props = {
  width: CSSProperties['width'];
  placement: 'center' | 'left' | 'right';
};

const getPlacementStyle = (placement: Props['placement']) => {
  switch (placement) {
    case 'center': {
      return 'center';
    }
    case 'left': {
      return 'flex-start';
    }
    case 'right': {
      return 'flex-end';
    }
  }
};

function Spinner({ width, placement }: Props) {
  const spinnerAnimation = useLottie({ animationData: spinnerLottie }, { width });
  const placementStyle = getPlacementStyle(placement);

  return <StyledContainer placementStyle={placementStyle}>{spinnerAnimation.View}</StyledContainer>;
}

export default Spinner;
