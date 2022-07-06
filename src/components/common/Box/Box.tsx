import React from 'react';

import styled from '@emotion/styled';
import { CSSObject } from '@emotion/react';

type Props = React.PropsWithChildren<
  Pick<CSSObject, 'width' | 'height' | 'borderRadius' | 'padding'>
>;

function Box({
  children,
  width,
  height,
  borderRadius = '15px',
  padding
}: Props) {
  return (
    <StyledBox
      width={width}
      height={height}
      borderRadius={borderRadius}
      padding={padding}
    >
      {children}
    </StyledBox>
  );
}

const StyledBox = styled.div<
  Pick<CSSObject, 'width' | 'height' | 'borderRadius' | 'padding'>
>(
  ({ theme, width, height, borderRadius, padding }) => `
    width: ${width};
    height: ${height};
    border-radius: ${borderRadius};
    box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_25};
    background-color: ${theme.colors.WHITE_100};
    padding: ${padding}
  `
);

export default Box;
