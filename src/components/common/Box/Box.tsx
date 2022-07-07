import React, { CSSProperties, PropsWithChildren, HTMLAttributes } from 'react';

import styled from '@emotion/styled';

interface Props extends PropsWithChildren<HTMLAttributes<HTMLDivElement>> {}

// TODO: min-height를 사용해도 될까?
function Box({ children, ...props }: Props & CSSProperties) {
  return <StyledBox {...props}>{children}</StyledBox>;
}

const StyledBox = styled.div<CSSProperties>(
  ({ theme, width, height, borderRadius, padding, minHeight }) => `
    width: ${width};
    height: ${height};
    min-height: ${minHeight};
    border-radius: ${borderRadius || '15px'};
    box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_25};
    background-color: ${theme.colors.WHITE_100};
    padding: ${padding}
  `
);

export default Box;
