import styled from '@emotion/styled';
import React, { CSSProperties, ProgressHTMLAttributes } from 'react';

interface Props extends ProgressHTMLAttributes<HTMLProgressElement> {}

function Progress({ ...props }: Props & CSSProperties) {
  return <StyledProgress {...props} />;
}

const StyledProgress = styled.progress<CSSProperties>(
  ({ width, theme, padding }) => `
  accent-color: ${theme.colors.PURPLE_100};
  width: ${width};
  padding: ${padding || '0.8rem'};
`
);

export default Progress;
