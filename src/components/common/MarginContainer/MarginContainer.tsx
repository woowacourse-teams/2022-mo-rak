import React from 'react';

import styled from '@emotion/styled';
import { CSSObject } from '@emotion/react';

interface Props extends React.PropsWithChildren, Pick<CSSObject, 'margin'> {}

function MarginContainer({ children, margin }: Props) {
  return <Container margin={margin}>{children}</Container>;
}

const Container = styled.div<Props>(
  ({ margin }) => `
  margin: ${margin};
`
);

export default MarginContainer;
