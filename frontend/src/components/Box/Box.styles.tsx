import { CSSProperties } from 'react';

import styled from '@emotion/styled';

// NOTE: overflow와 height 속성 추가....
const StyledContainer = styled.div<CSSProperties>(
  ({ theme, width, borderRadius, padding, minHeight, filter, height, overflow }) => `
    min-height: ${minHeight};
    border-radius: ${borderRadius || '15px'};
    box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_100_25};
    background-color: ${theme.colors.WHITE_100};
    ${width && `width: ${width}`};
    ${height && `height: ${height}`};
    ${padding && `padding: ${padding}`};
    ${filter && `filter: ${filter}`};
    ${overflow && `overflow: ${overflow}`};
  `
);

export { StyledContainer };
