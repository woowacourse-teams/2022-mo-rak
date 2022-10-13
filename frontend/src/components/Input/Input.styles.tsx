import { CSSProperties } from 'react';

import styled from '@emotion/styled';

const StyledInput = styled.input<CSSProperties>(
  ({ color, fontSize, textAlign, theme }) => `
    width: 100%;
    color: ${color || theme.colors.BLACK_100};
    text-align: ${textAlign || 'center'};
    ${fontSize && `font-size: ${fontSize}`};
  `
);

export { StyledInput };
