import { CSSProperties } from 'react';

import styled from '@emotion/styled';

const StyledContainer = styled.div<
  CSSProperties & {
    variantStyle: string;
  }
>(
  // TODO: text-align이 여기에 있는 게 맞을까??
  ({ width, borderRadius, variantStyle, padding, maxWidth }) => `
    ${variantStyle};
    position: relative;
    text-align: center;
    ${maxWidth && `max-width: ${maxWidth}`};
    ${width && `width: ${width}`};
    ${borderRadius && `border-radius: ${borderRadius}`};
    ${padding && `padding: ${padding}`};
  `
);

export { StyledContainer };
