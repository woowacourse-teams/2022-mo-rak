import { PropsWithChildren, CSSProperties, HTMLAttributes } from 'react';

import { StyledContainer } from './MarginContainer.styles';

interface Props extends PropsWithChildren<HTMLAttributes<HTMLDivElement>> {}

function MarginContainer({ children, ...props }: Props & CSSProperties) {
  return <StyledContainer {...props}>{children}</StyledContainer>;
}

export default MarginContainer;
