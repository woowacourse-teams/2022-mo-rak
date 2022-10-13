import { CSSProperties } from 'react';

import styled from '@emotion/styled';

const StyledButton = styled.button<
  CSSProperties & {
    variantStyle: string;
  }
>(
  // TODO: 긴급! padding 기본값 없애기
  // TODO: button의 width는 언제나 100% 아닐까? 찾아보자
  ({ width, padding, borderRadius, fontSize, variantStyle, disabled }) => `
  position: relative;
  text-align: center;
  border-radius: ${borderRadius || '1.6rem'};
  width: ${width || 'auto'};
  padding: ${padding || '1.2rem 0'};
  ${disabled && 'cursor: default'};
  ${fontSize && `font-size: ${fontSize}`};
  ${variantStyle}
  `
);

export { StyledButton };
