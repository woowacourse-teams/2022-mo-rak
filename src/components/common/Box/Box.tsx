import React from 'react';

import styled from '@emotion/styled';
import { CSSObject } from '@emotion/react';

type Props = React.PropsWithChildren<
  Pick<CSSObject, 'width' | 'height' | 'borderRadius'>
>;

function Box({ children, width, height, borderRadius = '15px' }: Props) {
  return (
    <Wrapper width={width} height={height} borderRadius={borderRadius}>
      {children}
    </Wrapper>
  );
}

const Wrapper = styled.div<
  Pick<CSSObject, 'width' | 'height' | 'borderRadius'>
>(
  ({ theme, width, height, borderRadius }) => `
    width: ${width};
    height: ${height};
    border-radius: ${borderRadius};
    box-shadow: 0px 4px 4px ${theme.colors.TRANSPARENT_BLACK_25};
    background-color: ${theme.colors.WHITE_100};
  `
);

export default Box;
