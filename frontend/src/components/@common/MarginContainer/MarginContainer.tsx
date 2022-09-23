import { PropsWithChildren, CSSProperties, HTMLAttributes } from 'react';

import styled from '@emotion/styled';

interface Props extends PropsWithChildren<HTMLAttributes<HTMLDivElement>> {}

function MarginContainer({ children, ...props }: Props & CSSProperties) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

const StyledContainer = styled.div<CSSProperties>(
  ({ margin }) => `
  margin: ${margin};
`
);

export default MarginContainer;
