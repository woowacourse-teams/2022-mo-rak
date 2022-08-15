import React, { CSSProperties, PropsWithChildren, HTMLAttributes } from 'react';

import styled from '@emotion/styled';

interface Props extends PropsWithChildren<HTMLAttributes<HTMLDivElement>> {}

function Box({ children, ...props }: Props & CSSProperties) {
  return <StyledBox {...props}>{children}</StyledBox>;
}

// TODO: minheight의 기본값을 주는 게 맞을까?
// NOTE: overflow와 height 속성 추가....
const StyledBox = styled.div<CSSProperties>(
  ({ theme, width, borderRadius, padding, minHeight, filter, height, overflow }) => `
    min-height: ${minHeight || '65.2rem'};
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

export default Box;
