import { useLottie } from 'lottie-react';
import { CSSProperties } from 'react';
import spinnerLottie from '../../assets/spinner-animation.json';
import { StyledContainer } from './Spinner.styles';

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
  // TODO: lottie 네이밍 통일 (다른 곳)
  const spinnerAnimation = useLottie({ animationData: spinnerLottie }, { width });
  const placementStyle = getPlacementStyle(placement);

  return <StyledContainer placementStyle={placementStyle}>{spinnerAnimation.View}</StyledContainer>;
}

export default Spinner;
