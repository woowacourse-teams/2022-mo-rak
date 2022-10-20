import { useLottie } from 'lottie-react';
import { CSSProperties } from 'react';
import spinner from '../../assets/spinner.json';
import { StyledContainer } from './CenteredSpinner.styles';

type Props = {
  width: CSSProperties['width'];
};

function CenteredSpinner({ width }: Props) {
  // TODO: lottie 네이밍 통일 (다른 곳)
  const spinnerAnimation = useLottie({ animationData: spinner }, { width });

  return <StyledContainer>{spinnerAnimation.View}</StyledContainer>;
}

export default CenteredSpinner;
