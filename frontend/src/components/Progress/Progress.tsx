import { CSSProperties, ProgressHTMLAttributes } from 'react';
import { StyledProgress } from '@/components/Progress/Progress.styles';

type Props = {
  accentColor?: string | undefined;
} & ProgressHTMLAttributes<HTMLProgressElement> &
  CSSProperties;

function Progress({ accentColor, ...props }: Props) {
  return <StyledProgress accentColor={accentColor} {...props} />;
}

export default Progress;
