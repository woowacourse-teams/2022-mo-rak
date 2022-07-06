import React from 'react';

import styled from '@emotion/styled';
import { CSSObject } from '@emotion/react';

type Props = React.PropsWithChildren<
  Pick<CSSObject, 'width' | 'height' | 'borderRadius' | 'padding' | 'minHeight'>
>;

// TODO: min-height를 사용해도 될까?
function Box({ children, width, height, minHeight, borderRadius = '15px', padding }: Props) {
  return (
    <StyledBox
      width={width}
      height={height}
      minHeight={minHeight}
      borderRadius={borderRadius}
      padding={padding}
    >
      {children}
    </StyledBox>
  );
}

const StyledBox = styled.div<
  Pick<CSSObject, 'width' | 'height' | 'borderRadius' | 'padding' | 'minHeight'>
>(
  ({ theme, width, height, borderRadius, padding, minHeight }) => `
    width: ${width};
    height: ${height};
    min-height: ${minHeight};
    border-radius: ${borderRadius};
    box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_25};
    background-color: ${theme.colors.WHITE_100};
    padding: ${padding}
  `
);

export default Box;
