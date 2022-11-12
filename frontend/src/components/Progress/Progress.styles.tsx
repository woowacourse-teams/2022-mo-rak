import { CSSProperties } from 'react';

import styled from '@emotion/styled';

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

export { StyledProgress };
