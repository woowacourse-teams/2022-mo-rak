import styled from '@emotion/styled';
import { CSSProperties, ProgressHTMLAttributes } from 'react';

interface Props extends ProgressHTMLAttributes<HTMLProgressElement> {
  accentColor?: string | undefined;
}

function Progress({ accentColor, ...props }: Props & CSSProperties) {
  return <StyledProgress accentColor={accentColor} {...props} />;
}

const StyledProgress = styled.progress<
  CSSProperties & {
    accentColor: string | undefined;
  }
>(
  ({ width, theme, padding, accentColor }) => `
  accent-color: ${accentColor || theme.colors.PURPLE_100};
  padding: ${padding || '0.8rem'};
  ${width && `width: ${width}`};
`
);

export default Progress;
