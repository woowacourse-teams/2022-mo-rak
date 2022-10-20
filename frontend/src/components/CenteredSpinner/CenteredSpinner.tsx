import { useLottie } from 'lottie-react';
import { CSSProperties } from 'react';
import spinnerAnimation from '../../assets/spinner.json';
import { StyledContainer } from './Spinner.styles';

type Props = {
  width: CSSProperties['width'];
};

function CenteredSpinner({ width }: Props) {
  // TODO: lottie 네이밍 통일
  const spinnerLottie = useLottie({ animationData: spinnerAnimation }, { width });

  return <StyledContainer>{spinnerLottie.View}</StyledContainer>;
}

export default CenteredSpinner;
