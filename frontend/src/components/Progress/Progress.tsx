import { CSSProperties, ProgressHTMLAttributes } from 'react';
import { StyledProgress } from './Progress.styles';

interface Props extends ProgressHTMLAttributes<HTMLProgressElement> {
  accentColor?: string | undefined;
}

function Progress({ accentColor, ...props }: Props & CSSProperties) {
  return <StyledProgress accentColor={accentColor} {...props} />;
}

export default Progress;
